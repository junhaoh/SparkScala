package com.junhaohuang.spark

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._

object PurchaseByCustomer {
  
  def parseLine(line: String) = {
  val fields = line.split(",")
  (fields(0).toInt, fields(2).toFloat)
}
  
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)
    
    val sc = new SparkContext("local[*]", "PurchaseByCustomer")
    val input = sc.textFile("customer-orders.csv")
    
    val parsedLines = input.map(parseLine)
    
    val customerPair = parsedLines.reduceByKey((x, y) => (x + y))
    
    val flipped = customerPair.map(x => (x._2, x._1))
    
    val customerSorted = flipped.sortByKey()
    
    val results = customerSorted.collect()
    
    results.sorted.foreach(println)
    
  }
}