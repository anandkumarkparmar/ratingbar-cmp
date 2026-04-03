#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
GROUP_PATH="${HOME}/.m2/repository/com/github/anandkumarkparmar"
REPORT_PATH="${ROOT_DIR}/.github/artifact-size-report.md"
BADGE_DIR="${ROOT_DIR}/.github/badges"
SUMMARY_ENV_PATH="${ROOT_DIR}/.github/artifact-size-summary.env"

ENFORCE_BUDGETS=false
if [[ "${1:-}" == "--enforce" ]]; then
  ENFORCE_BUDGETS=true
fi

MAX_ANDROID_AAR_BYTES="${MAX_ANDROID_AAR_BYTES:-102400}"
MAX_TOTAL_PUBLISHED_BYTES="${MAX_TOTAL_PUBLISHED_BYTES:-5242880}"

file_size_bytes() {
  stat -f%z "$1" 2>/dev/null || stat -c%s "$1"
}

to_kb() {
  awk "BEGIN {printf \"%.2f\", $1/1024}"
}

smart_size() {
  local bytes=$1
  if (( bytes >= 1048576 )); then
    awk "BEGIN {printf \"%.2f MB\", $bytes/1048576}"
  else
    awk "BEGIN {printf \"%.2f KB\", $bytes/1024}"
  fi
}

infer_platform() {
  local artifact_name="$1"
  local file_name="$2"
  local normalized
  normalized="$(printf '%s %s' "$artifact_name" "$file_name" | tr '[:upper:]' '[:lower:]')"

  if [[ "$normalized" == *"android"* ]] || [[ "$file_name" == *.aar ]]; then
    echo "Android"
  elif [[ "$normalized" == *"desktop"* ]]; then
    echo "Desktop"
  elif [[ "$normalized" == *"iossimulatorarm64"* ]]; then
    echo "iOS Simulator (arm64)"
  elif [[ "$normalized" == *"iosx64"* ]]; then
    echo "iOS Simulator (x64)"
  elif [[ "$normalized" == *"iosarm64"* ]]; then
    echo "iOS Device (arm64)"
  elif [[ "$normalized" == *"js"* ]]; then
    echo "Web (JS)"
  elif [[ "$normalized" == *"metadata"* ]]; then
    echo "Common Metadata"
  elif [[ "$normalized" == *"kotlinmultiplatform"* ]]; then
    echo "Multiplatform Root"
  else
    echo "Other"
  fi
}

mkdir -p "${BADGE_DIR}"

if [[ ! -d "${GROUP_PATH}" ]]; then
  echo "Maven local group path not found: ${GROUP_PATH}" >&2
  echo "Run ./gradlew :ratingbar-cmp:publishToMavenLocal first." >&2
  exit 1
fi

declare -a rows

total_bytes=0
android_aar_bytes=0
desktop_published_bytes=0
ios_published_bytes=0
web_published_bytes=0

while IFS= read -r artifact_dir; do
  artifact_name="$(basename "$artifact_dir")"
  version_dir="$(find "$artifact_dir" -mindepth 1 -maxdepth 1 -type d | sort -V | tail -n 1)"

  if [[ -z "${version_dir}" ]]; then
    continue
  fi

  while IFS= read -r artifact_file; do
    file_name="$(basename "$artifact_file")"

    if [[ "$file_name" == *"-sources."* ]] || [[ "$file_name" == *"-javadoc."* ]]; then
      continue
    fi

    size_bytes="$(file_size_bytes "$artifact_file")"
    platform="$(infer_platform "$artifact_name" "$file_name")"
    size_kb="$(to_kb "$size_bytes")"

    rows+=("| ${platform} | ${artifact_name} | ${file_name} | ${size_bytes} | ${size_kb} KB |")
    total_bytes=$((total_bytes + size_bytes))

    case "$platform" in
      "Desktop")
        desktop_published_bytes=$((desktop_published_bytes + size_bytes))
        ;;
      "Web (JS)")
        web_published_bytes=$((web_published_bytes + size_bytes))
        ;;
      "iOS Device (arm64)"|"iOS Simulator (arm64)"|"iOS Simulator (x64)")
        ios_published_bytes=$((ios_published_bytes + size_bytes))
        ;;
    esac

    if [[ "$file_name" == *.aar ]] && [[ "$platform" == "Android" ]]; then
      android_aar_bytes="$size_bytes"
    fi
  done < <(find "$version_dir" -maxdepth 1 -type f \( -name "*.aar" -o -name "*.jar" -o -name "*.klib" \) | sort)
done < <(find "$GROUP_PATH" -mindepth 1 -maxdepth 1 -type d -name 'ratingbar-cmp*' | sort)

if [[ ${#rows[@]} -eq 0 ]]; then
  echo "No published artifacts were found under ${GROUP_PATH}." >&2
  echo "Run ./gradlew :ratingbar-cmp:publishToMavenLocal first." >&2
  exit 1
fi

{
  echo "## Artifact Size Matrix"
  echo
  echo "Generated on: $(date -u +"%Y-%m-%d %H:%M:%S UTC")"
  echo
  echo "| Platform | Artifact | File | Size (bytes) | Size |"
  echo "|---|---|---|---:|---:|"
  for row in "${rows[@]}"; do
    echo "$row"
  done
  echo "| **Total (all published artifacts)** |  |  | **${total_bytes}** | **$(to_kb "$total_bytes") KB** |"
} > "$REPORT_PATH"

if [[ "$android_aar_bytes" -eq 0 ]]; then
  android_aar_bytes="$(find "${ROOT_DIR}/ratingbar-cmp/build/outputs/aar" -type f -name '*.aar' 2>/dev/null | head -n 1 | xargs -I {} sh -c 'stat -f%z "{}" 2>/dev/null || stat -c%s "{}"' 2>/dev/null || echo 0)"
fi

android_kb="$(to_kb "$android_aar_bytes")"
desktop_kb="$(to_kb "$desktop_published_bytes")"
ios_kb="$(to_kb "$ios_published_bytes")"
web_kb="$(to_kb "$web_published_bytes")"
total_kb="$(to_kb "$total_bytes")"

cat > "${BADGE_DIR}/android-aar-size.json" <<EOF
{
  "schemaVersion": 1,
  "label": "Android AAR",
  "message": "$(smart_size "$android_aar_bytes")",
  "color": "brightgreen"
}
EOF

cat > "${BADGE_DIR}/total-published-size.json" <<EOF
{
  "schemaVersion": 1,
  "label": "Total Published",
  "message": "$(smart_size "$total_bytes")",
  "color": "blue"
}
EOF

cat > "${BADGE_DIR}/desktop-published-size.json" <<EOF
{
  "schemaVersion": 1,
  "label": "Desktop",
  "message": "$(smart_size "$desktop_published_bytes")",
  "color": "blue"
}
EOF

cat > "${BADGE_DIR}/ios-published-size.json" <<EOF
{
  "schemaVersion": 1,
  "label": "iOS",
  "message": "$(smart_size "$ios_published_bytes")",
  "color": "lightgrey"
}
EOF

cat > "${BADGE_DIR}/web-published-size.json" <<EOF
{
  "schemaVersion": 1,
  "label": "Web",
  "message": "$(smart_size "$web_published_bytes")",
  "color": "orange"
}
EOF

{
  echo "ANDROID_AAR_BYTES=${android_aar_bytes}"
  echo "TOTAL_PUBLISHED_BYTES=${total_bytes}"
  echo "DESKTOP_PUBLISHED_BYTES=${desktop_published_bytes}"
  echo "IOS_PUBLISHED_BYTES=${ios_published_bytes}"
  echo "WEB_PUBLISHED_BYTES=${web_published_bytes}"
  echo "ANDROID_AAR_KB=${android_kb}"
  echo "TOTAL_PUBLISHED_KB=${total_kb}"
  echo "DESKTOP_PUBLISHED_KB=${desktop_kb}"
  echo "IOS_PUBLISHED_KB=${ios_kb}"
  echo "WEB_PUBLISHED_KB=${web_kb}"
} > "$SUMMARY_ENV_PATH"

if [[ "$ENFORCE_BUDGETS" == true ]]; then
  if (( android_aar_bytes > MAX_ANDROID_AAR_BYTES )); then
    echo "Android AAR budget failed: ${android_aar_bytes} > ${MAX_ANDROID_AAR_BYTES}" >&2
    exit 1
  fi

  if (( total_bytes > MAX_TOTAL_PUBLISHED_BYTES )); then
    echo "Total published size budget failed: ${total_bytes} > ${MAX_TOTAL_PUBLISHED_BYTES}" >&2
    exit 1
  fi
fi

echo "Artifact size report: ${REPORT_PATH}"
echo "Badge JSON files: ${BADGE_DIR}/android-aar-size.json, ${BADGE_DIR}/total-published-size.json, ${BADGE_DIR}/desktop-published-size.json, ${BADGE_DIR}/ios-published-size.json, ${BADGE_DIR}/web-published-size.json"
echo "Summary env file: ${SUMMARY_ENV_PATH}"
echo "Android AAR: ${android_aar_bytes} bytes (${android_kb} KB)"
echo "Desktop published: ${desktop_published_bytes} bytes (${desktop_kb} KB)"
echo "iOS published: ${ios_published_bytes} bytes (${ios_kb} KB)"
echo "Web published: ${web_published_bytes} bytes (${web_kb} KB)"
echo "Total published artifacts: ${total_bytes} bytes (${total_kb} KB)"
