package com.rekoe.utils;

import java.io.UnsupportedEncodingException;

public class SystemContext {

	public static final int PAGE_SIZE = 20;

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(decodeUnicode("cmsVoteTopic.function=\u6295\u7968\u7ba1\u7406"));
		System.out.println(decodeUnicode("cmsVoteTopic.function.add=\u6dfb\u52a0\u6295\u7968"));
		System.out.println(decodeUnicode("cmsVoteTopic.title=\u6807\u9898"));
		System.out.println(decodeUnicode("cmsVoteTopic.description=\u63cf\u8ff0"));
		System.out.println(decodeUnicode("cmsVoteTopic.time=\u6295\u7968\u65f6\u95f4\u9650\u5236"));
		System.out.println(decodeUnicode("cmsVoteTopic.startTime=\u5f00\u59cb\u65f6\u95f4"));
		System.out.println(decodeUnicode("cmsVoteTopic.endTime=\u7ed3\u675f\u65f6\u95f4"));
		System.out.println(decodeUnicode("cmsVoteTopic.time.help=\u7559\u7a7a\u4e0d\u9650\u5236"));
		System.out.println(decodeUnicode("cmsVoteTopic.repeateHour=\u91cd\u590d\u6295\u7968\u65f6\u95f4"));
		System.out.println(decodeUnicode("cmsVoteTopic.repeateHour.help=\u5355\u4f4d\uff1a\u5c0f\u65f6\u3002\u4e3a\u7a7a\u7981\u6b62\u91cd\u590d\u6295\u7968\uff1b0\u4e3a\u4e0d\u9650\u5236\u91cd\u590d\u6295\u7968\u3002"));
		System.out.println(decodeUnicode("cmsVoteTopic.totalCount=\u603b\u6295\u7968\u6570"));
		System.out.println(decodeUnicode("cmsVoteTopic.multiSelect=\u6700\u591a\u80fd\u9009\u51e0\u9879"));
		System.out.println(decodeUnicode("cmsVoteTopic.restrictMember=\u9650\u5236\u7528\u6237"));
		System.out.println(decodeUnicode("cmsVoteTopic.restrictMember.help=\uff08\u662f\uff1a\u5fc5\u987b\u767b\u5f55\u624d\u80fd\u6295\u7968\uff1b\u5426\uff1a\u5141\u8bb8\u6e38\u5ba2\u6295\u7968\uff09"));
		System.out.println(decodeUnicode("cmsVoteTopic.restrictIp=\u9650\u5236IP"));
		System.out.println(decodeUnicode("cmsVoteTopic.restrictCookie=\u9650\u5236COOKIE"));
		System.out.println(decodeUnicode("cmsVoteTopic.restrictCookie.help=\uff08\u53ef\u4ee5\u8bc6\u522b\u540c\u4e00IP\u7684\u4e0d\u540c\u7535\u8111\uff09"));
		System.out.println(decodeUnicode("cmsVoteTopic.disabled=\u6682\u505c"));
		System.out.println(decodeUnicode("cmsVoteTopic.def=\u9ed8\u8ba4"));
		System.out.println(decodeUnicode("cmsVoteTopic.subTopics=\u8c03\u67e5\u9898\u76ee"));
		System.out.println(decodeUnicode("cmsVoteTopic.type=\u9898\u76ee\u7c7b\u578b"));
		System.out.println(decodeUnicode("cmsVoteTopic.type.single=\u5355\u9009"));
		System.out.println(decodeUnicode("cmsVoteTopic.type.mul=\u591a\u9009"));
		System.out.println(decodeUnicode("cmsVoteTopic.type.text=\u6587\u672c"));
		System.out.println(decodeUnicode("cmsVoteTopic.addSubTopic=\u589e\u52a0\u8c03\u67e5\u9898\u76ee"));
		System.out.println(decodeUnicode("cmsVoteItem.splitchar=,"));
		System.out.println(decodeUnicode("cmsVoteItem.hasNoOne=\u6ca1\u6709\u5b50\u9009\u9879"));
		System.out.println(decodeUnicode("cmsVoteTopic.items=\u6295\u7968\u9879"));
		System.out.println(decodeUnicode("cmsVoteItem.title=\u6807\u9898"));
		System.out.println(decodeUnicode("cmsVoteItem.voteCount=\u7968\u6570"));
		System.out.println(decodeUnicode("cmsVoteItem.priority=\u6392\u5e8f"));
		System.out.println(decodeUnicode("cmsVoteItem.addLines=\u65b0\u589e\u6295\u7968\u9879"));
		System.out.println(decodeUnicode("cmsVoteTopic.log.save=\u589e\u52a0\u6295\u7968"));
		System.out.println(decodeUnicode("cmsVoteTopic.log.update=\u4fee\u6539\u6295\u7968"));
		System.out.println(decodeUnicode("cmsVoteTopic.log.delete=\u5220\u9664\u6295\u7968"));
	}

	/**
	 * unicode 转换成 中文
	 * 
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}
}