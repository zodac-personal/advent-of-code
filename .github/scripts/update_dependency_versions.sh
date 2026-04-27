#!/bin/bash
# ------------------------------------------------------------------------------
# Script Name:     update_dependency_versions.sh
#
# Description:     Updates version-pinned dependency declarations in pom.xml
#                  and GitHub Actions workflow files. Fetches the latest versions
#                  of Java, Maven, and GitHub Actions, then rewrites the relevant
#                  files with those values.
#
# Usage:           ./update_dependency_versions.sh
#
# Requirements:
#   - bash, jq, curl, mvn
#   - Internet access to fetch latest versions from Adoptium, GitHub, and Maven
#
# Behavior:
#   - For pom.xml:
#       - Updates Java major version (<java-release>)
#       - Updates Maven version (<maven-release>)
#       - Runs `mvn versions:update-properties` to update all other properties
#   - For .github/workflows:
#       - Updates java-version and maven-version fields
#       - Updates all GitHub Actions to their latest releases
#
# Exit Codes:
#   - 0: Success
#   - 1: Failure due to failed version fetches
# ------------------------------------------------------------------------------

set -euo pipefail

update_pom_versions() {
    local pom_xml="./pom.xml"
    local tmp_log
    tmp_log=$(mktemp)

    local checksum_before
    checksum_before=$(sha256sum "${pom_xml}" | cut -d' ' -f1)

    if ! mvn versions:update-properties -q --no-transfer-progress >"${tmp_log}" 2>&1; then
        cat "${tmp_log}" >&2
        rm -f "${tmp_log}"
        return 1
    fi
    rm -f "${tmp_log}"

    local checksum_after
    checksum_after=$(sha256sum "${pom_xml}" | cut -d' ' -f1)

    if [[ "${checksum_before}" != "${checksum_after}" ]]; then
        echo "✅ pom.xml updated with latest Maven packages"
    else
        echo "  pom.xml already up-to-date (no changes)"
    fi
}

update_maven_version() {
    local workflows_dir=".github/workflows"
    local pom_xml="./pom.xml"

    echo
    echo "🔍 Fetching latest Maven version..."

    local curl_args=(-fsSL)
    if [[ -n "${GITHUB_TOKEN:-}" ]]; then
        curl_args+=(-H "Authorization: Bearer ${GITHUB_TOKEN}")
    fi

    local latest_version
    latest_version=$(curl "${curl_args[@]}" "https://api.github.com/repos/apache/maven/releases/latest" | jq -r '.tag_name // empty')
    if [[ -z "${latest_version}" ]]; then
        echo "⚠️ Could not fetch latest Maven version from GitHub" >&2
        return 1
    fi
    latest_version="${latest_version#maven-}"

    echo "  Latest Maven version: ${latest_version}"

    # pom.xml
    sed -i "s|<maven-release>[^<]*</maven-release>|<maven-release>${latest_version}</maven-release>|" "${pom_xml}"

    # All workflow files
    if [[ -d "${workflows_dir}" ]]; then
        for workflow in "${workflows_dir}"/*.yml; do
            if grep -q "maven-version:" "${workflow}"; then
                sed -i "s|maven-version: '[0-9.]*'|maven-version: '${latest_version}'|g" "${workflow}"
            fi
        done
    fi

    echo "✅ Maven updated to ${latest_version} in pom.xml and workflows"
}

update_java_version() {
    local workflows_dir=".github/workflows"
    local pom_xml="./pom.xml"

    echo
    echo "🔍 Fetching latest Java version..."

    local available_releases
    available_releases=$(curl -fsSL "https://api.adoptium.net/v3/info/available_releases")
    local latest_major
    latest_major=$(echo "${available_releases}" | jq -r '.most_recent_feature_release // empty')
    if [[ -z "${latest_major}" ]]; then
        echo "⚠️ Could not fetch latest Java version from Adoptium" >&2
        return 1
    fi

    echo "  Latest Java major version: ${latest_major}"

    # pom.xml (major version only)
    sed -i "s|<java-release>[0-9]*</java-release>|<java-release>${latest_major}</java-release>|" "${pom_xml}"

    # All workflow files (major version only)
    if [[ -d "${workflows_dir}" ]]; then
        for workflow in "${workflows_dir}"/*.yml; do
            if grep -q "java-version:" "${workflow}"; then
                sed -i "s|java-version: '[0-9]*'|java-version: '${latest_major}'|g" "${workflow}"
            fi
        done
    fi

    echo "✅ Java updated to ${latest_major} in pom.xml and workflows"
}

get_github_action_version() {
    local action="${1}"
    local curl_args=(-fsSL)

    if [[ -n "${GITHUB_TOKEN:-}" ]]; then
        curl_args+=(-H "Authorization: Bearer ${GITHUB_TOKEN}")
    fi

    local version
    version=$(curl "${curl_args[@]}" "https://api.github.com/repos/${action}/releases/latest" | jq -r '.tag_name // empty')
    if [[ -z "${version}" ]]; then
        echo "⚠️ Could not fetch GitHub release version for ${action}" >&2
        return 1
    fi
    echo "${version}"
}

update_github_actions() {
    local workflows_dir=".github/workflows"

    if [[ ! -d "${workflows_dir}" ]]; then
        echo "⚠️ No workflows directory found at ${workflows_dir}, skipping"
        return 0
    fi

    echo
    echo "🔍 Fetching latest GitHub Action versions..."

    # Collect unique 'owner/repo@version' references from all workflow files
    mapfile -t action_refs < <(
        grep -rh 'uses:' "${workflows_dir}"/*.yml \
            | grep -oP 'uses:\s+\K[a-zA-Z0-9._-]+/[a-zA-Z0-9._-]+@\S+' \
            | sort -u
    )

    if [[ "${#action_refs[@]}" -eq 0 ]]; then
        echo "  No action references found in ${workflows_dir}"
        return 0
    fi

    for ref in "${action_refs[@]}"; do
        action="${ref%@*}"
        current_version="${ref#*@}"

        if ! latest_version=$(get_github_action_version "${action}"); then
            continue
        fi

        if [[ "${current_version}" == "${latest_version}" ]]; then
            echo "  ${action}=${latest_version} (already up-to-date)"
        else
            echo "  ${action}: ${current_version} → ${latest_version}"
            for workflow in "${workflows_dir}"/*.yml; do
                sed -i "s|${action}@${current_version}|${action}@${latest_version}|g" "${workflow}"
            done
        fi
    done

    echo "✅ ${workflows_dir} updated successfully with latest GitHub Actions"
}

update_pom_versions                         || echo "⚠️ Maven versions update failed, continuing..."
update_maven_version                        || echo "⚠️ Maven version update failed, continuing..."
update_java_version                         || echo "⚠️ Java version update failed, continuing..."
update_github_actions                       || echo "⚠️ GitHub Actions update failed, continuing..."
