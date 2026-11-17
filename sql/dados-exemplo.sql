-- OPCIONAL - só para demonstração/teste do gráfico e do histórico.
-- Gera conclusões FICTÍCIAS nos 6 dias anteriores para os hábitos de UMA conta.
-- Troque o e-mail abaixo pelo da conta que você quer popular e execute.
-- (Não use esses dados como evidência de uso real no dossiê.)

USE habit_tracker;

SET @email = 'seu-email@exemplo.com';

INSERT IGNORE INTO conclusoes (habito_id, data)
SELECT h.id, d.dia
FROM habitos h
JOIN usuarios u ON u.id = h.usuario_id
JOIN (SELECT CURDATE() - INTERVAL 1 DAY AS dia UNION ALL SELECT CURDATE() - INTERVAL 2 DAY
      UNION ALL SELECT CURDATE() - INTERVAL 3 DAY UNION ALL SELECT CURDATE() - INTERVAL 4 DAY
      UNION ALL SELECT CURDATE() - INTERVAL 5 DAY UNION ALL SELECT CURDATE() - INTERVAL 6 DAY) d
WHERE u.email = @email
  AND MOD(h.id + DAYOFMONTH(d.dia), 3) <> 0;   -- deixa alguns dias em branco

-- Para apagar as conclusões fictícias dessa conta depois:
-- DELETE c FROM conclusoes c JOIN habitos h ON h.id = c.habito_id JOIN usuarios u ON u.id = h.usuario_id
--  WHERE u.email = @email AND c.data < CURDATE();
