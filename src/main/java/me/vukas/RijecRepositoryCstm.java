package me.vukas;

import java.util.Optional;

public interface RijecRepositoryCstm {
	Optional<Rijec> findByRijec(String rijec);
	Rijec saveN(Rijec rijec);
}
