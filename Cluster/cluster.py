import jieba
import os
import numpy as np
def stopWord(path):
    file = open(path, encoding='utf-8')
    stopWords = []
    for line in file:
        stopWords.append(line.split('\n')[0])
    file.close()
    return set(stopWords)

def delStopWords(words,stop_words_set):
#   words是已经切词但是没有去除停用词的文档。
#   返回的会是去除停用词后的文档
    result = jieba.cut(words)
    new_words = []
    for r in result:
        if r not in stop_words_set:
            new_words.append(r)
    return new_words

def get_all_vector(file_path,stop_words_set):
    mFile = open("matrix.txt", mode='w', encoding='utf-8')
    names = [ os.path.join(file_path,f) for f in os.listdir(file_path) ]
    posts = [ open(name, mode='r' ,encoding="utf-8").read() for name in names ]
    docs = []
    word_set = set()
    for post in posts:
        doc = delStopWords(post, stop_words_set)
        docs.append(doc)
        word_set |= set(doc)
        #print len(doc),len(word_set)

    word_set = list(word_set)
    docs_vsm = []
    #for word in word_set[:30]:
        #print word.encode("utf-8"),
    for doc in docs:
        temp_vector = []
        for word in word_set:
            temp_vector.append(doc.count(word) * 1.0)
        #print temp_vector[-30:-1]
        docs_vsm.append(temp_vector)

    docs_matrix = np.array(docs_vsm)
    # print(docs_matrix)
    mFile.write(str(docs_matrix))
    mFile.close()
    column_sum = [ float(len(np.nonzero(docs_matrix[:,i])[0])) for i in range(docs_matrix.shape[1]) ]
    column_sum = np.array(column_sum)
    column_sum = docs_matrix.shape[0] / column_sum
    idf =  np.log(column_sum)
    idf =  np.diag(idf)
    # 请仔细想想，根绝IDF的定义，计算词的IDF并不依赖于某个文档，所以我们提前计算好。
    # 注意一下计算都是矩阵运算，不是单个变量的运算。
    for doc_v in docs_matrix:
        if doc_v.sum() == 0:
            doc_v = doc_v / 1
        else:
            doc_v = doc_v / (doc_v.sum())
    tfidf = np.dot(docs_matrix,idf)
    # print(tfidf)
    return names,tfidf


def gen_sim(A,B):
    num = float(np.dot(A,B.T))
    denum = np.linalg.norm(A) * np.linalg.norm(B)
    if denum == 0:
        denum = 1
    cosn = num / denum
    sim = 0.5 + 0.5 * cosn
    return sim
def randCent(dataSet, k):
    n = np.shape(dataSet)[1]
    centroids = np.mat(np.zeros((k,n)))#create centroid mat
    for j in range(n):#create random cluster centers, within bounds of each dimension
        minJ = min(dataSet[:,j])
        rangeJ = float(max(dataSet[:,j]) - minJ)
        centroids[:,j] = np.mat(minJ + rangeJ * np.random.rand(k,1))
    return centroids

def kMeans(dataSet, k, distMeas=gen_sim, createCent=randCent):
    m = np.shape(dataSet)[0]
    clusterAssment = np.mat(np.zeros((m,2)))#create mat to assign data points
                                      #to a centroid, also holds SE of each point
    centroids = createCent(dataSet, k)
    clusterChanged = True
    counter = 0
    while counter <= 50:
        counter += 1
        clusterChanged = False
        for i in range(m):#for each data point assign it to the closest centroid
            minDist = np.inf;
            minIndex = -1
            for j in range(k):
                distJI = distMeas(centroids[j,:],dataSet[i,:])
                if distJI < minDist:
                    minDist = distJI;
                    minIndex = j
            if clusterAssment[i,0] != minIndex:
                clusterChanged = True
            clusterAssment[i,:] = minIndex,minDist**2
        #print centroids
        for cent in range(k):#recalculate centroids
            ptsInClust = dataSet[np.nonzero(clusterAssment[:,0].A==cent)[0]]#get all the point in this cluster
            centroids[cent,:] = np.mean(ptsInClust, axis=0) #assign centroid to mean
    return centroids, clusterAssment


K = 3
file0 = open("0.txt", mode='w', encoding="utf-8")
file1 = open("1.txt", mode='w', encoding="utf-8")
file2 = open("2.txt", mode='w', encoding="utf-8")
names, tfdif = get_all_vector("./txt", "stopword.txt")
centroids, clusterAssment = kMeans(tfdif, K)
# print(clusterAssment[:,0])
for tup in zip(clusterAssment[:,0], names):
    # print(tup)
    for i in tup[0]:
        if(i == [[0.]]):
            file0.write(tup[1].split('.')[1].split('\\')[1] + "\n")
        if(i == [[1.]]):
            file1.write(tup[1].split('.')[1].split('\\')[1] + "\n")
        if(i == [[2.]]):
            file2.write(tup[1].split('.')[1].split('\\')[1] + "\n")
file0.close()
file1.close()
file2.close()
