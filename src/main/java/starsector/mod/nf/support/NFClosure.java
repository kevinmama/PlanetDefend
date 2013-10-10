package starsector.mod.nf.support;

public interface NFClosure<PT, RT>{
	RT execute(PT input);
}
