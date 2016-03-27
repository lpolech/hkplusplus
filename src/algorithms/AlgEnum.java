package algorithms;

public enum AlgEnum {
	KMEANS("Kmeans"),
	GMM("GMM");
	
	private final String algName;
	
	AlgEnum(String algName)
	{
		this.algName = algName;
	}
	
	public String toString()
	{
		return algName;
	}
}
