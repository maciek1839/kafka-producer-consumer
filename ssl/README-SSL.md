# Generate SSL certificate

1. Install OpenSSL
   - https://tecadmin.net/install-openssl-on-windows/
2. Run script `` (using bash)
    ```
    export COUNTRY=US
    export STATE=IL
    export ORGANIZATION_UNIT=SE
    export CITY=Chicago
    export PASSWORD=secret
    bash ./kafka-generate-ssl-automatic.sh
   ```
   - Both scripts are copied from Confluent repository:
        - https://github.com/confluentinc/confluent-platform-security-tools
