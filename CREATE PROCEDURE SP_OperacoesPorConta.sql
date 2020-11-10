CREATE PROCEDURE SP_OperacoesPorConta
    @idConta BIGINT
AS
BEGIN 
    SELECT
        id, 
        tipo,
        valor,
        IdDestino,
        IdOrigem
    FROM Operacao WHERE IdDestino = @idConta
    UNION
    SELECT
        id,
        tipo,
        valor,
        IdDestino,
        IdOrigem
    FROM Operacao WHERE IdOrigem = @idConta
END 

EXEC SP_OperacoesPorConta 3