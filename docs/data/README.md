# Sobre os Dados do Sistema

O sistema foi desenvolvido para gerenciar encontros de mães da igreja, organizando informações sobre os encontros, as participantes e os serviços desempenhados em cada evento.

As principais entidades são:

- Mothers (Mães): armazena os dados pessoais das mães participantes, como nome, CPF, e-mail e telefone.
- Services (Serviços): representa os tipos de serviços realizados nos encontros, como acolhida, música, lanche e limpeza.
- Meets (Encontros): contém as informações de cada encontro, incluindo data e hora (DATETIME), endereço e status.
- Enlace: é a tabela de ligação que conecta uma mãe a um serviço em um determinado encontro, representando quem realizou qual função em cada evento.

Essa estrutura permite que o sistema registre de forma organizada quem participou de cada encontro e em qual serviço atuou, garantindo flexibilidade e clareza no gerenciamento das atividades do grupo.