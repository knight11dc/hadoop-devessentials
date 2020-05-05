--Load text into a bag, where each row is a line of text
lines = LOAD ' /home/mapr/Desktop/mapr_training/datasets/exercise6.txt' AS (line:chararray);

--Tokenize the provided text.
tokens = FOREACH lines GENERATE flatten(TOKENIZE(line)) AS token:chararray;

--Group by token.
distinctTokens = GROUP tokens BY token;

--Count distinct tokens and the number of times it occurs in the corpus.
countPerToken = FOREACH distinctTokens GENERATE group as token:chararray, COUNT(tokens) as tokenCount:long;

--Arrange the items in the group in descending order by count.
countsPerTokenOrdered = ORDER countPerToken BY tokenCount DESC;

--Calculate the first five elements which have the most occurring words
result = LIMIT countsPerTokenOrdered 5;

--Persist result to the HDFS.
STORE result INTO '/home/mapr/Desktop/mostOccuredTokens';