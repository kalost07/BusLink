import requests
import json

response = requests.post(
    "https://www.sofiatraffic.bg/bg/trip/getSchedule",
    json=json.loads(
        '{"line_id":168,"name":"7А","ext_id":"TB30","type":4,"color":"#2AA9E0","icon":"/images/transport_types/trolley.png","isWeekend":0}'
    ),
    headers={
        "Accept": "application/json, text/plain, */*",
        "Accept-Encoding": "gzip, deflate, br, zstd",
        "Accept-Language": "en-US,en;q=0.5",
        "Connection": "keep-alive",
        "Content-Type": "application/json",
        "Origin": "https://www.sofiatraffic.bg",
        "Priority": "u=0",
        "Referer": "https://www.sofiatraffic.bg/bg/public-transport",
        "Sec-Fetch-Dest": "empty",
        "Sec-Fetch-Mode": "cors",
        "Sec-Fetch-Site": "same-origin",
        "User-Agent": "Mozilla/5.0",
        "X-Requested-With": "XMLHttpRequest",
        "X-XSRF-TOKEN": "top-secret :)",
    },
    cookies={
        "XSRF-TOKEN": "top-secret :)",
        "cf_clearance": "top-secret :)",
        "sofia_traffic_session": "top-secret :)",
    },
    auth=(),
)

if response.status_code == 200:
    with open("schedules/trolley_7A.json", "w", encoding="utf-8") as f:
        try:
            json.dump(response.json(), f, indent=2, ensure_ascii=False)
            print("json dumped to " + f.name)
        except:
            print("couldn't dump json")
            print(response.text)
    print("Download successful.")
else:
    print("Request failed:", response.status_code, response.text)
