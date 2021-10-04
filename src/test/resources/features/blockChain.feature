# language: es
Característica: BLOCKCHAIN
  Como usuario
  Quiero validar el hash
  Para confirmar que el bloque es correcto

  @Block_prueba
  Escenario: Validar que el numero de bloque prevBlock
    Cuando Ejecuto el servicio "blockcypher" para validar el bloque
    Entonces  Guardo el response de la ejecucion del servicio
    Y Valido que la respuesta contenga el atributo "hash" y "prev_block"
    Dado que configuro la cabecera del servicio
      | cabeceras        | valor                                                                                                             |
      | Accept           | application/json, text/plain, */*                                                                                 |
      | Referer          | https://api.blockcypher.com                                                                                       |
      | Origin           | https://api.blockcypher.com                                                                                       |
      | User-Agent       | Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36 |
      | Sec-Fetch-Mode   | cors                                                                                                              |
      | Content-Type     | application/json; charset=UTF-8                                                                                   |
      | Sec-Fetch-Site   | cors                                                                                                              |
      | Sec-Fetch-Site   | same-origin                                                                                                       |
      | Accept-Enconding | gzip, deflate, br                                                                                                 |
      | Host             | api.blockcypher.com                                                                                               |
      | Accept-Language  | es,en;q=0.9                                                                                                       |
      | hash             | hash ${hash}                                                                                                      |
      | Connection       | keep-alive                                                                                                        |
      | prev_block       | prev_block${prev_block}                                                                                           |
      | ciam-token       | Bearer ${token}                                                                                                   |
    Entonces  Guardo el response de la ejecucion del servicio