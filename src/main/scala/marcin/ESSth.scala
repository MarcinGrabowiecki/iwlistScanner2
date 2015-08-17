package marcin

import org.elasticsearch.node.NodeBuilder._

/**
 * Created by m on 2015-05-30.
 */
class ESSth extends Printer{
  val node = nodeBuilder()
    .clusterName("elasticsearch")
    .node();
  val client = node.client();

  val parse={
    val saga=new Saga()
    saga.printer=this;
    saga.proc(new LogReader);
    node.close();
  }

  override def print(line: String): Unit = {
    println("indexing:"+line)
    val id=client
      .prepareIndex("iwlist","iwlist")
      .setSource(line)
      .execute()
      .actionGet()
      .getId()
    println(id)
  }
}
