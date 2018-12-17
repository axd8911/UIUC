import metapy

def extract_sequences(tok):
    sequences = []
    for token in tok:
        if token == '<s>':
            sequences.append(metapy.sequence.Sequence())
        elif token != '</s>':
            sequences[-1].add_symbol(token)
    return sequences


tagger = metapy.sequence.PerceptronTagger("perceptron-tagger/")

doc = metapy.index.Document()
doc.content("I said that I can't believe that it only costs $19.95!")
tok = metapy.analyzers.ICUTokenizer()
tok = metapy.analyzers.PennTreebankNormalizer(tok)
tok.set_content(doc.content())
for seq in extract_sequences(tok):
    tagger.tag(seq)
    print(seq)


