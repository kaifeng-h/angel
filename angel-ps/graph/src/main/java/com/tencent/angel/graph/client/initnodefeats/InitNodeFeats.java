package com.tencent.angel.graph.client.initnodefeats;

import com.tencent.angel.graph.data.Node;
import com.tencent.angel.ml.math2.vector.IntFloatVector;
import com.tencent.angel.ml.matrix.psf.update.base.PartitionUpdateParam;
import com.tencent.angel.ml.matrix.psf.update.base.UpdateFunc;
import com.tencent.angel.ps.storage.matrix.ServerMatrix;
import com.tencent.angel.ps.storage.partition.RowBasedPartition;
import com.tencent.angel.ps.storage.vector.ServerIntAnyRow;

public class InitNodeFeats extends UpdateFunc {

  /**
   * Create a new UpdateParam
   */
  public InitNodeFeats(InitNodeFeatsParam param) {
    super(param);
  }

  public InitNodeFeats() {
    this(null);
  }

  @Override
  public void partitionUpdate(PartitionUpdateParam partParam) {
    PartInitNodeFeatsParam param = (PartInitNodeFeatsParam) partParam;
    ServerMatrix matrix = psContext.getMatrixStorageManager().getMatrix(partParam.getMatrixId());
    RowBasedPartition part = (RowBasedPartition) matrix
        .getPartition(partParam.getPartKey().getPartitionId());
    ServerIntAnyRow row = (ServerIntAnyRow) part.getRow(0);

    int[] nodeIds = param.getNodeIds();
    IntFloatVector[] feats = param.getFeats();

    for (int i = 0; i < nodeIds.length; i++) {
      Node node = (Node)row.get(nodeIds[i]);
      if(node == null) {
        node = new Node();
        row.set(nodeIds[i], node);
      }
      node.setFeats(feats[i]);
    }
  }
}
