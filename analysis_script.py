#!/usr/bin/python3

import subprocess

uppaal_bin = '/Users/ezer/uppaal64-4.1.24/bin-Darwin/verifyta'
model_base = '/Users/ezer/Documents/ms/research/logic-model/chemCaseStudy.xml'
query = '/Users/ezer/Documents/ms/research/logic-model/chemCaseStudy.q'

model_temp = '/Users/ezer/Documents/ms/research/logic-model/chemCaseStudy_temp.xml'


def update_value(n):
    with open(model_base, 'r') as f:
        file_data = f.read()

    file_data = file_data.replace('attack_time = 0', 'attack_time = {}'.format(n))

    with open(model_temp, 'w') as f:
        f.write(file_data)


def print_content(filepath):
    with open(filepath, 'r') as f:
        data = f.readlines()
    for line in data:
        if len(line)>3 and len(line)<50:
            print(line)


def main():
    launch_command = "{} {} {}".format(uppaal_bin, model_temp, query)
    print_content(query)

    for i in range(16):
        update_value(i)
        process = subprocess.Popen(launch_command, stdin=subprocess.PIPE, 
                stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
        output, errors = process.communicate()

        if "NOT" in str(output):
            print(i, "not satisfied")
        else:
            print(i, "satisfied")


if __name__ == '__main__':
    main()
