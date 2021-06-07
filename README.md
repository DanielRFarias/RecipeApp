RecipeApp

Este aplicativo tem como função exibir uma lista contendo receitas retiradas da api encontrada no site http://www.recipepuppy.com/about/api/.

Ele atende 3 requisitos considerados obrigatórios para sua implementação, sendo eles:

-Login social: deve-se utilizar algum sistema de login. Ex: Facebook, Google+, e-mail...

-Listagem de receitas: a listagem deve conter informações básicas como nome, imagem.

-Detalhe da receita: imagem grande da receita, lista de ingredientes, informações, botão para acessar o site original.

O aplicativo foi implementado usando kotlin no android studio versão 4.1.3 

A tela inicial possui as opções de login e de registro, o registro é necessário para adicionar uma conta que será usada com o login sendo necessário usar um endereço válido e uma senha com ao menos 6 caracteres.

A opção de registro leva a uma tela diferente, onde quando o registro é realizado com sucesso o usuário será redirecionado para a tela inicial onde é possível imediatamente realizar o login.

Para o Login foi usado firebase com a escolha de login usando email, para maior facilidade nos testes não foi usado verificação e uma versão mais antiga de firebase foi utilizada, tendo sido escolhida essa versão pelo tipo database que permite mapear diferentes atributos para um usuário, considerando possível necessidade futura de adicionar informações para diferentes usuários.

Na tela inicial onde receitas são exibidas, as receitas usadas são obtidas pelo endereço http://www.recipepuppy.com/api/, tocando em uma delas é possível obter mais detalhes e uma imagem maior da receita desejada, sendo os detalhes o nome, os ingredientes e um botão usado para acessar o site original, para manter o tamanho do texto foi usada apenas uma linha para p nome na tela inicial, ou seja, o nome que aparece pode estar abreviado.

A exibição dessas receitas foi usada para mostrar a aplicação dos requisitos obrigatórios, bem como para o propósito de testes relacionados à funcionalidade do aplicativo.

Na versão mais recente caso alguma receita tenha sido adicionada aos favoritos a lista de favoritos será exibida ao entrar.

Além disso no canto superior direito da tela é possível ver um botão de pesquisa, quando tocado ele move para o lado esquerdo, e se tocado novamente é possível realizar uma busca por novas receitas do site recipepuppy.

A busca pode ser feita através do nome em inglês do prato, por exemplo omelet, ou pelo(s) ingrediente(s) desejado(s).

Para buscar mais de um ingrediente é preciso digitar na barra de pesquisa os ingredientes, em inglês, podendo ser separados por vírgula ou com espaço entre eles, por exemplo:

-ham,cheese
-eggs bacon

Ao lado do botão de pesquisas existe três pontos, quando tocados duas opções aparecem, favourites e logout, como o nome sugere quando tocado em favorites a lista das receitas favoritas aparece na tela, e quando logout é usado o usuário retorna a tela de login inicial.

Ao tocar no coração abaixo do nome de uma receita ela é adicionada a lista de favoritos armazenada usando Room, se tocado novamente será excluída da lista, quando o coração está todo vermelho siginifica que se encontra na lista de favoritos, um coração branco com bordas pretas indica que a receita não se encontra na lista de favoritos.

No caso de o usuário ter os favoritos exibidos, seja por usar a opção favoritos na parte superior da tela, ou ao entrando no aplicativo com uma ou mais receitas salvas como favoritas, tocando o coração também remove a receita da tela.

Para o recebimento do json encontrado na url foi usado retrofit, bem como Gson, as imagens foram renderizadas com a biblioteca Picasso, e a lista é mostrada usando uma recyclerview.

Foram usadas ao todo 4 atividades, uma tela inicial com login, uma tela de registro, uma tela com as receitas, onde é possível realizar buscas, exibir as receitas favoritas e adicionar receitas a lista de favoritos e uma com detalhes de uma receita clicada.
