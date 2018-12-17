#!/usr/bin/python3

import math
import metapy
import sys
import time


def make_classifier(training, inv_idx, fwd_idx):
    """
    Use this function to train and return a Classifier object of your
    choice from the given training set. (You can ignore the inv_idx and
    fwd_idx parameters in almost all cases, but they are there if you need
    them.)

    **Make sure you update your config.toml to change your feature
    representation!** The data provided here will be generated according to
    your specified analyzer pipeline in your configuration file (which, by
    default, is going to be unigram words).

    Also, if you change your feature set and already have an existing
    index, **please make sure to delete it before running this script** to
    ensure your new features are properly indexed.
    """
    # ranker = metapy.index.OkapiBM25(k1=1.5,b=0.80,k3=5000)
    # ranker = metapy.index.OkapiBM25()

    # return metapy.classify.KNN(training, inv_idx, 4, ranker)
    # return metapy.classify.NaiveBayes(training)
    # return metapy.classify.NearestCentroid(training, inv_idx)

    return metapy.classify.OneVsAll(training, metapy.classify.SGD, loss_id='hinge')
    # kernel = metapy.classify.Kernel()
    # return metapy.classify.DualPerceptron(training, kernel)

if __name__ == '__main__':
    if len(sys.argv) != 2:
        print("Usage: {} config.toml".format(sys.argv[0]))
        sys.exit(1)

    metapy.log_to_stderr()

    cfg = sys.argv[1]
    print('Building or loading indexes...')
    inv_idx = metapy.index.make_inverted_index(cfg)
    fwd_idx = metapy.index.make_forward_index(cfg)

    dset = metapy.classify.MulticlassDataset(fwd_idx)

    # view = metapy.classify.MulticlassDatasetView(dset)
    # view.shuffle()

    print('Running cross-validation...')
    start_time = time.time()
    matrix = metapy.classify.cross_validate(lambda fold:
            make_classifier(fold, inv_idx, fwd_idx), dset, 5)

    print(matrix)
    matrix.print_stats()
    print("Elapsed: {} seconds".format(round(time.time() - start_time, 4)))
