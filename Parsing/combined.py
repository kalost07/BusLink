import requests
import json
import uncurl


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
  --data-raw '{"line_id":64,"name":"M1","ext_id":"M1","type":3,"color":"#2B3C8F","icon":"/images/transport_types/subway.png","isWeekend":0}'
"""
)

response = eval(str)
if response.status_code == 200:
    with open("schedules/metro_1.json", "w", encoding="utf-8") as f:
        try:
            json.dump(response.json(), f, indent=2, ensure_ascii=False)
            print("json dumped to " + f.name)
        except:
            print("couldn't dump json")
            print(response.text)
    print("Download successful.")
else:
    print("Request failed:", response.status_code, response.text)
