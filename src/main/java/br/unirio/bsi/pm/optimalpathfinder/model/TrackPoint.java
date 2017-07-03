package br.unirio.bsi.pm.optimalpathfinder.model;

/**
 * Represents a point on a track map
 * 
 * @author rodrigo.cezar@uniriotec.br
 */

public class TrackPoint {

	private boolean checkpoint;
	private boolean valid;
	private int checkpointIndex;

	public TrackPoint(boolean checkpoint, boolean valid) {
		this.checkpoint = checkpoint;
		this.valid = valid;
		this.checkpointIndex = 0;
	}

	public TrackPoint(boolean checkpoint, boolean valid, int checkpointIndex) {
		this.checkpoint = checkpoint;
		this.valid = valid;
		this.checkpointIndex = checkpointIndex;
	}

	public boolean isCheckpoint() {
		return checkpoint;
	}

	public void setCheckPoint(boolean checkPoint) {
		this.checkpoint = checkPoint;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public void setCheckpointIndex(int index) {
		this.checkpointIndex = index;
	}

	public int getCheckpointIndex() {
		return this.checkpointIndex;
	}

}