Para rodar o projeto precisamos instalar o Scene Builder, link:
</br>
https://gluonhq.com/products/scene-builder/

Ao baixar, veja em que pasta o Scene Builder foi instalado

Baixe a versão 21 do Java (a última versão estável)
</br>
https://www.oracle.com/br/java/technologies/downloads/#java21

E o JavaFX (21.0.8), versões acima não são compatíveis com o Java 21:
</br>
https://gluonhq.com/products/javafx/

Extraia o javaFx em alguma pasta, como em:
</br>
C:\java-libs\

Para rodar o JavaFX no intelliJ, tem que baixar a versão Ultimate e não a community:
</br>
https://www.jetbrains.com/idea/download/?section=windows

Ative uma licença de estudante para o JetBrains:
</br>
https://www.jetbrains.com/academy/student-pack/

E depois instale a biblioteca do JavaFX em:
</br>
File ≥ Settings.. ≥ Plugins ≥ Marketplace ≥ JavaFX

E depois indique o endereço do Scene Builder para o IntelliJ:
</br>
File ≥ Settings ≥ Languages & Frameworks ≥ JavaFx
</br>

E insira a pasta em que o Scene Builder está:
</br>
C:\Users\User\AppData\Local\SceneBuilder\SceneBuilder.exe

Para rodar o projeto, em cima escolha a opção do AppLauncher:
</br>
3 pontos ≥ Edit... ≥ Modify Options ≥ Add VM Options (Alt+v)
</br>
E no VM Options, cole o comando:
</br>
--module-path "C:\java-libs\javafx-sdk-21.0.8\lib" --add-modules javafx.controls,javafx.fxml

Veja em que pasta que você colocou o javafx

Para logar no programa, esses são os dados:
 - Login: admin
 - Senha: admin