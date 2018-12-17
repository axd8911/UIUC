import metapy
import time
import pytoml

idx = metapy.index.make_inverted_index('config.toml')
f = open("bm25.avg_p.txt","w")

query = metapy.index.Document()
ranker = metapy.index.OkapiBM25(k1=1.2, b=0.75,k3=500)

ev = metapy.index.IREval('config.toml')

# Load the query_start from config.toml or default to zero if not found
with open('config.toml', 'r') as fin:
        cfg_d = pytoml.load(fin)
query_cfg = cfg_d['query-runner']
query_start = query_cfg.get('query-id-start', 0)


start_time = time.time()
num_results = 10
with open('cranfield-queries.txt') as query_file:
    for query_num, line in enumerate(query_file):
        query.content(line.strip())
        results = ranker.score(idx, query, num_results)
        avg_p = ev.avg_p(results, query_num, num_results)
        print("Query {} average precision: {}".format(query_num + 1, avg_p))
        f.write("{}\n".format(avg_p))

ev.map()
print("Mean average precision: {}".format(ev.map()))
print("Elapsed: {} seconds".format(round(time.time() - start_time, 4)))
f.close()
