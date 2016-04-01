<#assign 	
	webtran = transaccion[0..1] + "WB"
	segtran = programa[0..3] 
>

ceda define tran(${webtran}) target(elis)
ceda define tran(${segtran}) target(elis)

ceda define tran(${transaccion}) target(tores)
ceda define tran(${transaccion}) target(aores)

ceda define prog(${programa}) target(tores)
ceda define prog(${programa}) target(aores)