import os
import fileinput
import sys


def is_int(s):
    try:
        int(s)
        return True
    except ValueError:
        return False


for folder in ['count', 'grep']:
    # for folder in ['count', 'grep']:
    for path, subdirs, files in os.walk(sys.argv[1]+folder):
        for name in files:
            file_path = os.path.join(path, name)

            f = open(file_path, 'r')

            query = path.split('_')[2]
            query = query.replace('.', '_')
            query = query.replace(':', '_')

            name = name.split('_')[0]
            name = name.replace('mb', '')

            if not is_int(name):
                name = "'" + name + "'"

            str_arr = folder + "['"+query + "'][" + name + "] = ["
            for line in f:
                if line.startswith('real'):
                    # print(line, end='')
                    line = line.replace('real\t', '')
                    line = line.replace('s', '')
                    line = line.replace('m', '*60+')
                    line = eval(line)
                    line = str(line) + ', '
                    str_arr += line
            str_arr += ']\n'
            print(str_arr, end='')
