file = open("event.json", mode='r', encoding='UTF-8')
# txtFile = open("seg.txt", mode='w', encoding='utf-8')
import json
jsonObject = json.load(file)
file.close()
total = jsonObject['pagination']['total']
for i in range(0, total):
    tmpFile = open("txt/" + jsonObject['data'][i]['_id'] + ".txt",mode='w', encoding="utf-8")
    tmpFile.write(jsonObject['data'][i]['seg_text'])
    tmpFile.close()
# txtFile.close()
print(total)
