import requests
import json
print(1)
url = "https://covid-dashboard.aminer.cn/api/events/list?type=event&page=1&size=1000"
response = requests.get(url)
res = json.loads(response.text)
# print(str(res))
file = open("event.json", mode='w',encoding='utf-8')
json.dump(res, file)
file.close()
# print(data)
