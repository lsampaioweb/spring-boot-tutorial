#!/usr/bin/env bash
# Generate a local self-signed PKCS12 for the HTTPS sample.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SSL_DIR="${SCRIPT_DIR}/src/main/resources/ssl"
KEYSTORE="${SSL_DIR}/my-cert.p12"
PASSWORD="${KEY_STORE_PASSWORD_MY_HTTPS_APP:-change-me}"

mkdir -p "${SSL_DIR}"

if [[ -f "${KEYSTORE}" ]]; then
  echo "Keystore already exists: ${KEYSTORE}"
  echo "Delete it first if you want to regenerate."
  exit 0
fi

keytool -genkeypair \
  -alias https-sample \
  -keyalg RSA \
  -keysize 2048 \
  -storetype PKCS12 \
  -keystore "${KEYSTORE}" \
  -validity 3650 \
  -storepass "${PASSWORD}" \
  -keypass "${PASSWORD}" \
  -dname "CN=localhost,OU=Tutorial,O=Learning,L=Local,ST=NA,C=US" \
  -ext "SAN=dns:localhost,ip:127.0.0.1"

echo "Created ${KEYSTORE}"
echo "Export before running the app:"
echo "  export KEY_STORE_PASSWORD_MY_HTTPS_APP=${PASSWORD}"
