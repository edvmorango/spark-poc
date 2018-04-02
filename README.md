Esse projeto consiste na extração de insights dos datasets encontrados no link abaixo:

```
http://ita.ee.lbl.gov/html/contrib/NASA-HTTP.html
```

As tecnologias utilizadas foram:

```
Java 8
Scala 2.11.12
Apache Spark 2.2.1
```

Instruções:
```
É necessário a criação de duas pastas no diretório do projeto: 
  "datasets" onde os arquivos do dataset devem estar descompactados.
  "result" onde será possível visualizar o agrupamento de erros por dia, particionados.
```


Questões:

Qual o objetivo do comando cache em Spark?
```
Basicamente os RDDs são "evaluated" de forma lazy(só passam a existir quando forem chamados pela primeira vez) e por questões de alocação de recursos do cluster, após a execução de uma operação/pipeline de operações qualquer os RDDs são apagados da memória, e reavaliados quando chamados novamente, o cache(memoization em termos de programação funcional) é basicamente uma função que mantém o conteúdo de um RDD após alguma operação, costuma ser utilizado em RDDs que serão "operados" a seguir.
```
O mesmo código implementado em Spark é normalmente mais rápido que a implementação equivalente em MapReduce. Por quê?
```
O processamento de dados no MapReduce se dá por leitura/escrita em disco, enquanto o Spark utiliza memória, apesar de normalmente ser para processamento em memória o Spark também suporta processamento por leitura/escrita em disco.
```
Qual é a função do SparkContext​?
```
É basicamente a conexão da aplicação em Spark com o cluster, sendo assim é responsável pela criação de RDDs, Broadcast variables e a execução de outros serviços providos pelo Spark.
```
Explique com suas palavras o que é Resilient​ ​Distributed​ ​Datasets​ (RDD)
```
RDD é um wrapper de dados que os faz existir no cluster, garantindo assim distribuição, paralelismo, lazy evaluation, tolerância a falhas e outros recursos. É a estrutura mais elementar do Spark, serve como base para DataFrames e DataSets.
```
groupByKey​ ​é menos eficiente que reduceByKey​ ​em grandes dataset. Por quê?
```
Basicamente o groupByKey acumula todos os valores distribuídos antes de começar o processo de folding, enquanto o reduceByKey consiste em fazer o folding em cada partição e após isso fazer o fold dos resultados das partições.
````
Explique o que o código Scala abaixo faz:
```
 val textFile = sc.textFile("hdfs://...")
 val counts = textFile.flatMap(line => line.split(" "))
         .map(word => (word, 1))
   	         .reduceByKey(_ + _)
 counts.saveAsTextFile("hdfs://...")
```
 	

```"textFile" lê um arquivo dentro do HDFS e empacota seus valores em um RDD.
A pipeline de "counts" basicamente quebra as linhas em palavras, as transforma em uma tupla, em seguida inicia o processo de folding, resultando em um count de palavras (palavra, frequência).
A última linha basicamente salva o count de palavras em um arquivo de texto no HDFS.
```
Insights:
```
1. Número de hosts únicos.
2. O total de erros 404.
3. Os 5 URLs que mais causaram erro 404.
4. Quantidade de erros 404 por dia.
5. O total de bytes retornados.
```


