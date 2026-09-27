package calendrica;

/*- chinese-name -*/

// TYPE (chinese-stem chinese-branch) -> chinese-name
// Returns bogus if stem/branch combination is impossible.

/*- chinese-stem -*/
/*- chinese-branch -*/

public class ChineseName {
	public int stem;
	public int branch;
	
	public ChineseName()
	{
		this.stem = 1;
		this.branch = 1;
	}
	
	public ChineseName(int stem, int branch)
		throws BogusDateException
	{
		if(Date.mod(stem, 2) == Date.mod(branch, 2)) {
			this.stem = stem;
			this.branch = branch;
		} else {
			throw new BogusDateException();
		}
	}
}
