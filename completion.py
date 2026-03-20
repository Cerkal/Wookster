#!/usr/bin/env python3
import hmac
import hashlib
import base64
import re

SECRET = b"you-be-more-careful"

def generate_code(input_value):
    digest = hmac.new(SECRET, input_value.encode(), hashlib.sha256).digest()
    encoded = base64.b64encode(digest).decode()
    encoded = re.sub(r'[^A-Z0-9]', '', encoded)
    return encoded[:4]

def verify_code(input_value, code):
    expected = generate_code(input_value)
    return expected == code


# get input from user
input_value = input("Enter the user ID to verify: ")
code = input("Enter the code to verify: ").upper()

if verify_code(input_value, code):
    print("✅ Code is authentic")
else:
    print("❌ Invalid code")