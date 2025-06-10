import uncurl

with open("uncurled.txt", "w", encoding="UTF-8") as f:
    str = uncurl.parse(
        """curl 'https://www.sofiatraffic.bg/bg/trip/getSchedule' \
  -X POST \
  -H 'User-Agent: Mozilla/5.0' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: en-US,en;q=0.5' \
  -H 'Accept-Encoding: gzip, deflate, br, zstd' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'Content-Type: application/json' \
  -H 'X-XSRF-TOKEN: top-secret :)' \
  -H 'Origin: https://www.sofiatraffic.bg' \
  -H 'Connection: keep-alive' \
  -H 'Referer: https://www.sofiatraffic.bg/bg/public-transport' \
  -H 'Cookie: top-secret :)' \
  -H 'Sec-Fetch-Dest: empty' \
  -H 'Sec-Fetch-Mode: cors' \
  -H 'Sec-Fetch-Site: same-origin' \
  -H 'Priority: u=0' \
  --data-raw $'{"line_id":168,"name":"7\u0410","ext_id":"TB30","type":4,"color":"#2AA9E0","icon":"/images/transport_types/trolley.png","isWeekend":0}'
"""
    )
    f.write(str)
    print("wrote command")
