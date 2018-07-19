<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<HTML>
	<HEAD>
		<meta http-equiv="Content-Language" content="zh-cn">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="${pageContext.request.contextPath}/css/Style1.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/layer/layer.js"></script>
		<script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery-1.11.3.min.js"></script>
		<script type="text/javascript">
			function showDetail(oid) {
				//alert(oid);
				$.post("${pageContext.request.contextPath}/adminOrder",{"method":"getDetailByOid","oid":oid},function(data){
					//alert(oid);
					var table="<table width='99%' border=1''><tr><th>商品名称</th><th>数量</th><th>单价</th><th>总价</th></tr>"
					$(data).each(function(){
						table+="<tr><td>"+this.product.pname+"</td><td>"+this.count+"</td><td>"+this.product.shop_price+"</td><td>"+this.subtotal+"</td></tr>"
					});
					table+="</table>" 
					layer.open({
						type:1,//o:信息框 1：页面 2：iframe层 3：加载层 4：tip层
						title:"订单详情",
						area:['800px','300px'],
						shadeClose:false,//点击层外区域遮罩关闭
						content:table//内容
					});
				},"json");
			}
		</script>
	</HEAD>
	<body>
		<br>
		<form id="Form1" name="Form1" action="${pageContext.request.contextPath}/user/list.jsp" method="post">
			<table cellSpacing="1" cellPadding="0" width="100%" align="center" bgColor="#f5fafe" border="0">
				<TBODY>
					<tr>
						<td class="ta_01" align="center" bgColor="#afd1f3">
							<strong>订单列表</strong>
						</TD>
					</tr>
					
					<tr>
						<td class="ta_01" align="center" bgColor="#f5fafe">
							<table cellspacing="0" cellpadding="1" rules="all"
								bordercolor="gray" border="1" id="DataGrid1"
								style="BORDER-RIGHT: gray 1px solid; BORDER-TOP: gray 1px solid; BORDER-LEFT: gray 1px solid; WIDTH: 100%; WORD-BREAK: break-all; BORDER-BOTTOM: gray 1px solid; BORDER-COLLAPSE: collapse; BACKGROUND-COLOR: #f5fafe; WORD-WRAP: break-word">
								<tr
									style="FONT-WEIGHT: bold; FONT-SIZE: 12pt; HEIGHT: 25px; BACKGROUND-COLOR: #afd1f3">

									<td align="center" width="10%">
										序号
									</td>
									<td align="center" width="10%">
										订单编号
									</td>
									<td align="center" width="10%">
										订单金额
									</td>
									<td align="center" width="10%">
										收货人
									</td>
									<td align="center" width="10%">
										订单状态
									</td>
									<td align="center" width="50%">
										订单详情
									</td>
								</tr>
									<c:forEach items="${pageBean.list }" var="order" varStatus="vs">
									
										<tr onmouseover="this.style.backgroundColor = 'white'"
											onmouseout="this.style.backgroundColor = '#F5FAFE';">
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="16%">
												${vs.count+(pageBean.currPage-1)*pageBean.pageSize }
											</td>
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="20%">
												${order.oid }
											</td>
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="16%">
												${order.total }
											</td>
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="16%">
												${order.name }
											</td>
											<td style="CURSOR: hand; HEIGHT: 22px" align="center"
												width="16%">
												<c:if test="${order.state==0 }">
													未付款
												</c:if>
												<c:if test="${order.state==1 }">
													<a href="${ pageContext.request.contextPath }/adminOrder?method=updateState&oid=${order.oid }&state=2"><font color="blue">未发货</font></a>
												</c:if>
												<c:if test="${order.state==2 }">
													已发货
												</c:if>
												<c:if test="${order.state==3 }">
													订单完成
												</c:if>
											
											</td>
											<td align="center" style="HEIGHT: 22px">
												<input type="button" value="订单详情"  onclick="showDetail('${order.oid}')"/>
											</td>
							
										</tr>
									</c:forEach>
							</table>
						</td>
					</tr>
				</TBODY>
			</table>
		</form>
		<!--分页 -->
		<div style="width:380px;margin:0 auto;margin-top:50px;">
			<ul class="pagination" style="text-align:center; margin-top:10px;">
				<!-- 判断是否是首页，如果是则pre按钮不可用 -->
				<c:if test="${pageBean.currPage==1 }">
					<li class="disabled"><a href="javascript:void(0)" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
				</c:if>
				<c:if test="${pageBean.currPage!=1 }">
					<c:if test="${state==-1 }">
						<li ><a href="${pageContext.request.contextPath}/adminOrder?method=findAllByState&currPage=${pageBean.currPage-1 }" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
					</c:if>
					<c:if test="${state!=-1 }">
						<li ><a href="${pageContext.request.contextPath}/adminOrder?method=findAllByState&state=${state }&currPage=${pageBean.currPage-1 }" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
					</c:if>
				</c:if>
				
				<!-- 展示所有页码 -->
				<c:forEach begin="${pageBean.currPage-5>0?pageBean.currPage-5:1 }" end="${pageBean.currPage+4>pageBean.totalPage?pageBean.totalPage:pageBean.currPage+4}" var="n">
				<!--判断是否是当前页 ,当前页序号加深色 -->
					<c:if test="${pageBean.currPage==n }">
						<li class="active"><a href="javascript:void(0)">${n }</a></li>
					</c:if>
					<c:if test="${pageBean.currPage!=n }">
						<c:if test="${state==-1 }">
							<li><a href="${pageContext.request.contextPath}/adminOrder?method=findAllByState&currPage=${n}">${n }</a></li>						
						</c:if>
						<c:if test="${state!=-1 }">
							<li><a href="${pageContext.request.contextPath}/adminOrder?method=findAllByState&state=${state }&currPage=${n}">${n }</a></li>					
						</c:if>
					</c:if>

				</c:forEach>
				
				<!-- 判断是否是尾页，如果是则next按钮不可用 -->
				<c:if test="${pageBean.currPage==pageBean.totalPage }">
					<li class="disabled">
						<a href="javascript:void(0)" aria-label="Next">
							<span aria-hidden="true">&raquo;</span>
						</a>
					</li>
				</c:if>
				<c:if test="${pageBean.currPage!=pageBean.totalPage }">
						<c:if test="${state==-1 }">
							<li>
								<a href="${pageContext.request.contextPath}/adminOrder?method=findAllByState&currPage=${pageBean.currPage+1 }" aria-label="Next">
									<span aria-hidden="true">&raquo;</span>
								</a>
							</li>
						</c:if>
						<c:if test="${state!=-1 }">
							<li>
								<a href="${pageContext.request.contextPath}/adminOrder?method=findAllByState&state=${state }&currPage=${pageBean.currPage+1 }" aria-label="Next">
									<span aria-hidden="true">&raquo;</span>
								</a>
							</li>
						</c:if>
				</c:if>
			</ul>
		</div>
		<!-- 分页结束=======================        -->		
	</body>
</HTML>

