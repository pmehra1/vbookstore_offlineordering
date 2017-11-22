<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
		<invoice xmlns="http://com.virtusa.sg/vbookstore/invoice">
			<customer-id><xsl:value-of select="order/customerid" /></customer-id>
			<date-received><xsl:value-of select="current-date()"/></date-received>
			<xsl:for-each select="order/items/item">
				<line-item>
					<item-id>
						<xsl:value-of select="id" />
					</item-id>
					<item-name>
						<xsl:value-of select="item" />
					</item-name>
					<item-author>
						<xsl:value-of select="author" />
					</item-author>
					<item-quantity>
						<xsl:value-of select="quantity" />
					</item-quantity>
					<item-price>
						<xsl:value-of select="price_per_item" />
					</item-price>
					<order-date>
						<xsl:value-of select="date" />
					</order-date>
				</line-item>
			</xsl:for-each>
		</invoice>
	</xsl:template>

</xsl:stylesheet>