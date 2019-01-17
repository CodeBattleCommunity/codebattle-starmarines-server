<%@ page import="com.epam.game.domain.Game" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>



<div id="table" class=".yellow-header">

	<table id="head" class="statHeader"><thead>
	<tr>
		<th>Время создания</th>
		<th>Название игры</th>
		<%--<th>Описание     </th>--%>
		<th>Победитель   </th>
	</tr>  </table>
	<div id="statistics">

		<%--<c:out value="${statistics}" />--%>
		<c:forEach items="${statistics}" var="game"> <%--LIST OF GAMES--%>
			        <%--<c:out value="${game}" />--%>
			<div class="yellow-header opac-black-container">
				<table>
					<tr>
			<th><c:out value="${game.timeCreated}" />                    </th>

			<th><c:out value="${game.title}"/></th>
			<%--<th><c:out value="${game.description}" />                    </th>--%>
			<th><c:out value="${game.statistics.get(0).user.userName}" /></th>
						<%--<th><c:out value="${game.statistics}" /></th>--%>
					</tr>
				</table>
			</div>
			<div>
			<table class='playersTable'>
				<thead>
				<tr>
					<th>Игрок             </th>
					<th>email             </th>
					<th>Последний ход     </th>
					<th>Количество дроидов</th>
					<th>Место             </th>
				</tr>
				</thead>
				<tbody>
			<c:forEach items="${game.statistics}" var="gameStats">
				<tr>
				    <td><c:out value="${gameStats.user.userName}" /></td>
					<td><c:out value="${gameStats.user.email}" />   </td>
					<td><c:out value="${gameStats.turnsSurvived}" /></td>
					<td><c:out value="${gameStats.unitsCount}" />   </td>
					<td><c:out value="${gameStats.place}" />        </td>
				</tr>
			</c:forEach>
				</tbody>
			   </table>
			</div>
		</c:forEach>
	</div>

</div>


<div id="commonStatistics" >
	<h3 class="abys-branded statHeader">Общая статистика</h3>
	<div class="opac-black-container abys-branded">
		<c:forEach items="${commonStatistics}" var="cs">
			<h2><c:out value="${cs.typeMessage}" /></h2>
			<p> всего игр:    <span class="float-right"><c:out value="${cs.gameCount}" /></span></p>
			<c:if test="${cs.typeMessage ne 'Тренировки'}">
			<p>	max ботов за игру: <span class="float-right"><c:out value="${cs.zergName} - ${cs.zergCount}" /></span></p>
			</c:if>
			<p>	max участий в играх: <span class="float-right"><c:out value="${cs.mostPlayedName} - ${cs.mostPlayedCount}" /></span></p>
			<p>	max количество побед:  <span class="float-right"><c:out value="${cs.fastestName} - ${cs.fastestNumberOfTurns}" /> </span></p>
			<br/>
			<br/>
		</c:forEach>

	<%--<h2 >По тренировкам</h2>--%>
	<%--<p> всего игр:    <span class="float-right"><c:out value="${TRAINING_LEVEL.gameCount}" /></span></p>--%>
	<%--<p>	самый зерг: <span class="float-right"><c:out value="${TRAINING_LEVEL.zergName}" /> <c:out value="${TRAINING_LEVEL.ZergCount}" /></span></p>--%>
	<%--<p>	самый упорный: <span class="float-right">частное количество игр</span></p>--%>
	<%--<p>	самый дерзкий:  <span class="float-right">минимальное количество ходов</span></p>--%>
		<%--<br/>--%>
	<%--<h2>По совместным боям</h2>--%>
	<%--<p> всего игр:    <span class="float-right">количество игр</span></p>--%>
	<%--<p>	самый зерг: <span class="float-right">количество ботов</span></p>--%>
	<%--<p>	самый упорный: <span class="float-right">частное количество игр</span></p>--%>
	<%--<p>	самый дерзкий: <span class="float-right">минимальное количество ходов</span> </p>--%>
		<%--<br/>--%>
	<%--<h2>По турнирам</h2>--%>
	<%--<p> всего игр:    <span class="float-right">количество игр</span></p>--%>
	<%--<p>	самый зерг(kkk): <span class="float-right">количество ботов</span></p>--%>
	<%--<p>	самый упорный: <span class="float-right">частное количество игр</span></p>--%>
	<%--<p>	самый дерзкий:  <span class="float-right">минимальное количество ходов</span></p>--%>
	</div>
</div>


	 <script>$(function() {
		 $( "#statistics" ).accordion({
			 heightStyle: "content"
		 })
	 })</script>

