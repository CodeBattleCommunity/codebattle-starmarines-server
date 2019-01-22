import sys
import requests

SERVER_ADDRESS='http://localhost:8080'
CLIENT_COUNT=30
PREFIX='loadtester'

def register(login, password, email, server_address):
    body={
        'name': login,
        'password': password,
        'repassword': password,
        'email': email,
        'agreed': 'true',
        '_agreed': 'on'
    }
    headers={
        'Content-Type': 'application/x-www-form-urlencoded'
    }
    requests.post('{}/signUp.html'.format(server_address), data=body, headers=headers)

if __name__ == '__main__':
    clients = int(sys.argv[1] if len(sys.argv) >=2 else CLIENT_COUNT)
    prefix = sys.argv[2] if len(sys.argv) >=3 else PREFIX
    address = sys.argv[3] if len(sys.argv) >=4 else SERVER_ADDRESS
    for i in range(0, clients):
        user = '{}{}'.format(prefix, i)
        register(user, user, user + 'exmaple.com', address)

