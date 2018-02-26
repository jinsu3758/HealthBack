from socket import *
import time
import threading

textdir = 'C:\\Users\\ccu03\\Desktop\\abc.txt'
def send_client(connect,addr):
    while True:
        with open(textdir) as f:
            sensor = f.readlines()
            x = f.tell()
        f = open(textdir, 'r')

        for i in range(4, x):
            f.seek(x - i)
            if (f.readline() == '\n'):
                break
        sensor = f.readline()+'\n'
        sensor = sensor.encode('utf-8')
        f.close()
        try:
            connect.send(sensor)
        except Exception as e:
            print("disconnect ",addr)
            exit()
        print(sensor)
        print('얍얍\n')
        #   f = open(textdir,'w')
        #    f.close()
        time.sleep(2)
host = ''
port = 4480
c_list = []
th = []
ssock = socket(AF_INET, SOCK_STREAM)
ssock.bind((host,port))
ssock.listen(5)
while True:
    print("hhhhhhhh")
    connect, addr = ssock.accept()
    c_list.append(connect)
    print('connect with : ',addr)
    client = threading.Thread(target=send_client, args=(connect,addr))
    client.start()
    th.append(client)
for t in th :
    t.join()
