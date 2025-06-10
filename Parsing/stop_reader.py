import json
from pathlib import Path


def processJSON(file):
    with open(file, "r", encoding="utf-8") as f:
        data = json.load(f)

    if (
        data["line"]["is_active"] == False
        or data["line"]["has_single_direction"] == True
    ):
        return
    # Insert type & name
    match data["line"]["type"]:
        case 1:
            routeOut.write("Bus ")
        case 2:
            routeOut.write("Tram")
        case 4:
            routeOut.write("Trolley")
        case _:
            routeOut.write("What")
    routeOut.write(" " + data["line"]["name"] + "\n")

    for route in data["routes"]:
        for segment in route["segments"]:
            id = segment["start_stop_id"]
            # Add to the set if not in there
            if id not in stops:
                stop = segment["stop"]
                stops[id] = {
                    "code": stop["code"],
                    "name": stop["name"],
                    "lon": stop["longitude"],
                    "lat": stop["latitude"],
                }
                stopOut.write(stops[id]["code"] + " " + stops[id]["name"] + "\n")
                stopOut.write(stops[id]["lon"] + "\n" + stops[id]["lat"] + "\n")
            routeOut.write(stops[id]["code"] + " ")
        routeOut.write("\n")


folder = Path("schedules")
stops = {}
with open("Output/stops.txt", "w", encoding="utf-8") as stopOut:
    with open("Output/routes.txt", "w", encoding="utf-8") as routeOut:
        for file in folder.glob("*.json"):
            if file.is_file():
                print("JSON file:", file)
                processJSON(file)
