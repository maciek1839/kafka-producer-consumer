# Generate SSL/TLS certificates using Confluent scripts

1. Install OpenSSL
   - https://tecadmin.net/install-openssl-on-windows/
2. Run script `` (using bash)
    ```
    export COUNTRY=US
    export STATE=IL
    export ORGANIZATION_UNIT=SE
    export CITY=Chicago
    export PASSWORD=secret
    export COMMON_NAME=localhost
    bash ./kafka-generate-ssl-automatic.sh
   ```
   - Both scripts are based on Confluent scripts from: https://github.com/confluentinc/confluent-platform-security-tools
   - If you run the script on Windows using Git Bash, you need to set one more variable: `export MSYS_NO_PATHCONV=1`. See details: https://stackoverflow.com/questions/54258996/git-bash-string-parameter-with-at-start-is-being-expanded-to-a-file-path
3. (Optional) Print JKS content, run this command in trustore/keystore folder: `keytool -v -list -keystore kafka.keystore.jks`

## An alternative script for complete TLS/SSL Kafka configuration

See an [instruction](../secrets/README-SECRETS.md)
