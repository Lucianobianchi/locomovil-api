import psycopg2
import csv

def saveToPOSTGRES(data):
    # Connect to POSTGRESQL database
    try:
        conn = psycopg2.connect("dbname='dataset-locomovil' user='Bianchi' host='localhost'")
    except:
        print('I am unable to connect to the database')

    cur = conn.cursor()

    # Insert all loaded projects
    for scan in data:
        print("Inserting\n", scan)

        projectInsert = {"name":"DS-PROJECT-" + str(scan['project_id']), "project_id": scan['project_id']}
        cur.execute("""INSERT INTO projects(name, project_id) VALUES (%(name)s, %(project_id)s) ON CONFLICT DO NOTHING; COMMIT;""", projectInsert)

        mapInsert = {"name":"DS-MAP-" + str(scan['map_id']), "project_id": scan['project_id'], "map_id": scan["map_id"]}
        cur.execute("""INSERT INTO maps(name, project_id, map_id) VALUES (%(name)s, %(project_id)s, %(map_id)s) ON CONFLICT DO NOTHING; COMMIT;""", mapInsert)

        cur.execute("""INSERT INTO scans(wifi_scan_id, coord_x, coord_y, ntp_time, map_id, project_id)
            VALUES (%(wifi_scan_id)s, %(coord_x)s, %(coord_y)s, %(ntp_time)s, %(map_id)s, %(project_id)s);""", scan)

        cur.executemany("""INSERT INTO wifi_scans(wifi_scan_id, bssid, level)
            VALUES (%(wifi_scan_id)s, %(bssid)s, %(level)s);""", scan["wifi"])

def readDataset(name):
    data = []
    # Read CSV and save data in objects.
    with open(name, 'rt') as csvfile:
        reader = csv.DictReader(csvfile)
        for idx, row in enumerate(reader):
            scan = {}
            wifis = []
            for i in range(1, 520):
                wifi = {}
                address = 'WAP' + str(i).zfill(3)
                level = float(row[address])
                if (level != 100):
                    wifi['bssid'] = address
                    wifi['level'] = level
                    wifi['wifi_scan_id'] = idx
                    wifis.append(wifi)
            scan['wifi'] = wifis
            scan['coord_y'] = float(row['LONGITUDE'])
            scan['coord_x'] = float(row['LATITUDE'])
            scan['ntp_time'] = int(row['TIMESTAMP'])
            scan['map_id'] = int(row['FLOOR'])
            scan['project_id'] = int(row['BUILDINGID'])
            scan['wifi_scan_id'] = idx
            data.append(scan)
    return data

# saveToPOSTGRES(readDataset('TrainingData.csv'))
print(readDataset('ValidationData.csv'), sep="\n")
