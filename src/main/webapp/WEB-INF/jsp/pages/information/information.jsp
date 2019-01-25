<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h2><spring:message code="label.information.header"/></h2>

<h3>Как происходит игра</h3>
<p>Сервер собирает команды от игроков, участвующих в игре. Раз в две секунды сервер пересчитывает карту с учетом полученных от игроков команд.
<p>Процесс пересчета юнитов в каждой вершине происходит следующим образом:
<ul>
	<li>Вычисляется количество юнитов хозяина планеты, с учетом улетевших с этой планеты и прилетевших на нее юнитов этого игрока.</li>
	<li>Получившееся количество увеличивается по правилам регенерации юнитов для этой планеты.</li>
	<li>После этого происходит выбор нового хозяина планеты (в случае, если на планету были посланы чьи-то еще юниты). Хозяином планеты становится игрок с наибольшим количеством юнитов. При этом юниты всех остальных игроков уничтожаются, а у победителя остается число юнитов, на которое он превосходил следующего по численности игрока.<br /><i>Например, если на планете после регенерации было 220 юнитов одного игрока, и еще двое прислали по 100 и 225 юнитов, хозяином планеты станет игрок, приславший 225 юнитов, и на планете останется только 5 юнитов.</i></li>
</ul>

<h3>Запуск тестовой игры</h3>
<p>Начать тестовую игру можно по ссылке на странице <a href='<c:url value="/showGames.html"></c:url>'>"<spring:message code="label.menu.viewGames" />"</a>. Будет создан тренировочный уровень. После этого игрок должен запустить своего бота (первый запрос к серверу будет задержан до начала игры), выбрать количество тренировочных ботов-соперников и начать игру.

<h3>Многопользовательская игра (турнир)</h3>
<p>Многопользовательская игра создается и запускается администратором игры. Список еще не начатых турниров можно видеть на странице <a href='<c:url value="/showGames.html"></c:url>'>"<spring:message code="label.menu.viewGames" />"</a>. Там же можно войти в турнир. Подключение происходит так же, как и к тестовой игре.

<h3>Как подключиться к серверу</h3>
<p>Сервер игры слушает порт 9080, приложение подключается к нему через клиентский сокет.

<h3>Что и как отправлять на сервер</h3>
<p>Идентификация игроков происходит по уникальному ключу, который присваивается каждому игроку при регистрации и может быть изменен по желанию игрока (при условии, что в этот момент игрок не участвует ни в одной игре). Узнать или изменить свой ключ можно на странице <a href='<c:url value="/profile.html"></c:url>'>"<spring:message code="label.menu.profile" />".</a>
<p>Запросы на сервер формируются в xml. Для начала игры нужно получить игровое поле, послав пустой список действий:
<pre class="code">
&lt;?xml version="1.0" encoding="UTF-8" standalone="no"?&gt;
&lt;request&gt;
	&lt;token&gt;abcdefghijklmnopqrstuvwxy&lt;/token&gt;
	&lt;actions&gt;
	&lt;/actions&gt;
&lt;/request&gt;
</pre>

Если пользователь не участвует ни в одной игре или игра уже закончена, сервер сразу пришлет ответ с соответствующей ошибкой, например:
<pre class="code">&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;
&lt;response&gt;
	&lt;planets /&gt;
	&lt;errors&gt;
		&lt;error&gt;User has not join any game&lt;/error&gt;
	&lt;/errors&gt;
&lt;/response&gt;
</pre>

Если игра начата, ответом будет описание игрового поля в следующем виде: 

<pre class="code">
&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;no&quot;?&gt;
	&lt;response&gt;
		&lt;planets&gt;
			&lt;planet id=&quot;1&quot;&gt;
				&lt;owner/&gt;
				&lt;type&gt;TYPE_A&lt;/type&gt;
				&lt;droids&gt;0&lt;/droids&gt;
				&lt;neighbours&gt;
					&lt;neighbour&gt;2&lt;/neighbour&gt;
					&lt;neighbour&gt;3&lt;/neighbour&gt;
				&lt;/neighbours&gt;
			&lt;/planet&gt;
			&lt;planet id=&quot;2&quot;&gt;
				&lt;owner&gt;bot&lt;/owner&gt;
				&lt;type&gt;TYPE_B&lt;/type&gt;
				&lt;droids&gt;55&lt;/droids&gt;
				&lt;neighbours&gt;
					&lt;neighbour&gt;1&lt;/neighbour&gt;
					&lt;neighbour&gt;3&lt;/neighbour&gt;
					&lt;neighbour&gt;5&lt;/neighbour&gt;
					&lt;neighbour&gt;6&lt;/neighbour&gt;
					&lt;neighbour&gt;7&lt;/neighbour&gt;
				&lt;/neighbours&gt;
			&lt;/planet&gt;
			
			...
			
		&lt;/planets&gt;
	&lt;errors/&gt;
&lt;/response&gt;
</pre>

В данном случае type - тип планеты - отвечает за прирост юнитов и предел, после достижения которого юниты перестанут регенерироваться.
 <br/>
TYPE_A: Прирост за ход +10%, Предел юнитов 100
<br/>
TYPE_B: Прирост за ход +15%, Предел юнитов 200
<br/>
TYPE_C: Прирост за ход +20% Предел юнитов 500
<br/>
TYPE_D: Прирост за ход +30% Предел юнитов 1000
<br/>
<br/>
Исходя из этих данных приложение игрока должно сформировать ответ. Ответ содержит список команд для передвижения юнитов, например:

<pre class="code">
&lt;?xml version="1.0" encoding="UTF-8" standalone="no"?&gt;
&lt;request&gt;
	&lt;token&gt;abcdefghijklmnopqrstuvwxy&lt;/token&gt;
	&lt;actions&gt;
		&lt;action&gt;
			&lt;from&gt;15&lt;/from&gt;
			&lt;to&gt;25&lt;/to&gt;
			&lt;unitscount&gt;1200&lt;/unitscount&gt;
		&lt;/action&gt;
		&lt;action&gt;
			&lt;from&gt;15&lt;/from&gt;
			&lt;to&gt;23&lt;/to&gt;
			&lt;unitscount&gt;100&lt;/unitscount&gt;
		&lt;/action&gt;
		&lt;action&gt;
			&lt;from&gt;15&lt;/from&gt;
			&lt;to&gt;21&lt;/to&gt;
			&lt;unitscount&gt;105&lt;/unitscount&gt;
		&lt;/action&gt;
	&lt;/actions&gt;
&lt;/request&gt;
</pre>

<p>Ответы на все ошибочные запросы отсылаются сервером сразу. Ответ на корректный запрос отсылается сервером после того, как в игре происходит следующий ход, и содержит описание игрового поля после этого хода. После отсылки сообщения сервер закрывает соединение, и все последующие действия потребуют создания новых соединений.

<h3>Пример</h3>
<p>Пример бота на java, совершающего один и тот же ход на каждом шаге:
<pre class="code">
public class StupidBot {
    
    private static String SERVER_ADDRESS = &quot;%server adress here%&quot;;
    private static int SERVER_PORT = 9080;
    private static String USER_TOKEN = &quot;abcdefghijklmnopqrstuvwxy&quot;;
    private static int BUFFER_SIZE = 16000;
    
    public static void main(String [] args) {
        boolean gameEnded = false;
        String request = null;
        while(!gameEnded){
            try{
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                InputStream sin = socket.getInputStream();
                OutputStream streamToServer = socket.getOutputStream();
                DataOutputStream out = new DataOutputStream(streamToServer);
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(sin));
                if(request == null){
                    request = generateInitialRequest();
                }
                System.out.println(&quot;Sending the request: &quot; + request);
                streamToServer.write(request.getBytes());
                streamToServer.flush();
                CharBuffer cb = CharBuffer.allocate(BUFFER_SIZE);
                responseReader.read(cb);
                cb.flip();
                responseReader.close();
                out.close();
                streamToServer.close();
                sin.close();
                socket.close();
                String serverResponse = new String(cb.array()).trim();
                System.out.println(&quot;Server response: &quot; + serverResponse);
                request = generateRequestByResponse(serverResponse);
            } catch(Exception e){
                System.err.println(&quot;Something gone wrong: &quot; + e.getMessage());
            }
        }
    }

    private static String generateRequestByResponse(String serverResponse) {
        return String.format(&quot;&lt;?xml version=\&quot;1.0\&quot; encoding=\&quot;UTF-8\&quot; standalone=\&quot;no\&quot;?&gt;&lt;request&gt;&lt;token&gt;%s&lt;/token&gt;&lt;actions&gt;&lt;action&gt;&lt;from&gt;9&lt;/from&gt;&lt;to&gt;7&lt;/to&gt;&lt;unitscount&gt;10&lt;/unitscount&gt;&lt;/action&gt;&lt;/actions&gt;&lt;/request&gt;&quot;, USER_TOKEN);
    }

    private static String generateInitialRequest() {
        return String.format(&quot;&lt;?xml version=\&quot;1.0\&quot; encoding=\&quot;UTF-8\&quot; standalone=\&quot;no\&quot;?&gt;&lt;request&gt;&lt;token&gt;%s&lt;/token&gt;&lt;actions&gt;&lt;login&gt;&lt;/login&gt;&lt;/actions&gt;&lt;/request&gt;&quot;, USER_TOKEN);
    }
}
</pre>
