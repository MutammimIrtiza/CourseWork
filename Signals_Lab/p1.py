from collections import defaultdict



n = int(input())
visits = [] # list of tuples



# dict of dict for values = int
# the lambda is called when a key is missing, creating an empty dict against that key
# note : keys can be any type, but ofc its best to stick to one type
patients = defaultdict(lambda : defaultdict(int))

for _ in range(n):
    name, severity, time = input().split()
    severity = int(severity)
    time = int(time)

    patients[name]["time"] += time 
    patients[name]["severity"] += severity

    visits.append((name, severity, time))

"""
alternative:

patients = {}   # also dict of dict
if name not in patients:
        patients[name] = {
            "time": 0,
            "severity": 0
        }
"""




# traversing a dict in sorted order of keys
for name in sorted(patients):
    print(name, 
          patients[name]["time"],
          patients[name]["severity"])
    



    
# patients.items() is a list of tuples of (name, inner_dict)
max = min(patients.items(), key = lambda x : ( -x[1]["severity"], x[0]) )
print("TOP", max[0], max[1]["severity"])

"""
alternative:

top_name = None 
top_score = -1

for name in patients:
    if patients[name]["severity"] > top_score:
        top_name = name
        top_score = patients[name]["severity"]
    elif patients[name]["severity"] == top_score and name < top_name:
        top_name = name

print("TOP", top_name, top_score)

"""


# more syntax:
names = set()
for name in sorted(names):
    pass

# a dictionary in descending order of keys:
total_time = {}
total_time_desc = dict(sorted(total_time.items(), reverse=True))


# a dictionary in descending order of some value:
severity_desc = dict(
    sorted(
        patients.items(),
        lambda x : x[1]["severity"],
        reverse=True
    )
)