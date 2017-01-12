class HardCodingCheck {
	  private final int f1 = 0;  // Noncompliant [[sc=22;ec=24]] {{Hardcoding of variables must be avoided.}}
	  String uname = "steve";// Noncompliant [[sc=11;ec=16]] {{Hardcoding of variables must be avoided.}}
	  private static final String BACK_SLASH = "\\"; // Noncompliant [[sc=32;ec=42]] {{Hardcoding of variables must be avoided.}}
	  public void mthd(){
	    private final int POSSIBLE = 4; // Noncompliant
	    private final String POSSIBLE_2 = ""; // Noncompliant
	  }
	  
	  String pname = Constants.NAME; // Compliant
	  int f2 = Constants.F2_NUMBER; // Compliant
	  
}

