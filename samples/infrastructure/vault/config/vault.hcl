ui            = true
disable_mlock = true
log_level     = "info"
log_file      = "/vault/logs/vault.log"
api_addr      = "http://127.0.0.1:8200"

storage "file" {
  path = "/vault/file"
}

listener "tcp" {
  address     = "0.0.0.0:8200"
  tls_disable = true
}
