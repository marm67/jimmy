package jimmy

package object jimmy {
	implicit def listRecursosCics2ListaElementoCics[ElementoCics](xs: List[RecursoCics]): ListaElementoCics = new ListaElementoCics(xs)
}