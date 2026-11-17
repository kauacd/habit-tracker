// Concluir hábito sem recarregar a página: a marca aparece na hora (feedback imediato)
// e o pedido vai para o servidor em segundo plano. Se falhar, desfaz e avisa.
// Sem JavaScript, o formulário continua funcionando normalmente.
(function () {
    'use strict';

    var contador = document.getElementById('qtd-feitos');

    function ajustarContador(delta) {
        if (contador) {
            contador.textContent = String(Number(contador.textContent) + delta);
        }
    }

    document.addEventListener('submit', function (evento) {
        var form = evento.target;
        if (!form.classList || !form.classList.contains('form-concluir')) {
            return;
        }
        evento.preventDefault();

        var corpo = new URLSearchParams(new FormData(form)).toString();
        var card = form.closest('.card');

        // 1) mostra como concluído imediatamente
        var marca = document.createElement('span');
        marca.className = 'check feito';
        marca.setAttribute('role', 'img');
        marca.setAttribute('aria-label', 'Concluído hoje');
        marca.textContent = '\u2713';
        form.replaceWith(marca);
        card.classList.add('feito');
        ajustarContador(1);

        // 2) confirma no servidor
        fetch(form.action, {
            method: 'POST',
            credentials: 'same-origin',
            headers: {
                'X-Requested-With': 'fetch',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: corpo
        }).then(function (resposta) {
            if (!resposta.ok) {
                throw new Error('status ' + resposta.status);
            }
        }).catch(function () {
            // 3) deu errado: volta ao estado anterior
            marca.replaceWith(form);
            card.classList.remove('feito');
            ajustarContador(-1);
            alert('Não foi possível concluir o hábito. Tente de novo.');
        });
    });
})();
