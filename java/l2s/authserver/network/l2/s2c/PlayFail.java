/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * http://www.gnu.org/copyleft/gpl.html
 */
package l2s.authserver.network.l2.s2c;

/**
 * This class ...
 *
 * @version $Revision: 1.2.4.1 $ $Date: 2005/03/27 15:30:11 $
 */
public final class PlayFail extends L2LoginServerPacket
{
	public static final PlayFail REASON_NO_MESSAGE = new PlayFail(0x00);
	public static final PlayFail REASON_SYSTEM_ERROR_LOGIN_LATER = new PlayFail(0x01);
	public static final PlayFail REASON_USER_OR_PASS_WRONG = new PlayFail(0x02);
	public static final PlayFail REASON_ACCESS_FAILED_TRY_AGAIN_LATER = new PlayFail(0x04);
	public static final PlayFail REASON_ACCOUNT_INFO_INCORRECT_CONTACT_SUPPORT = new PlayFail(0x05);
	public static final PlayFail REASON_ACCOUNT_IN_USE = new PlayFail(0x07);
	public static final PlayFail REASON_UNDER_18_YEARS_KR = new PlayFail(0x0C);
	public static final PlayFail REASON_SERVER_OVERLOADED = new PlayFail(0x0F);
	public static final PlayFail REASON_SERVER_MAINTENANCE = new PlayFail(0x10);
	public static final PlayFail REASON_TEMP_PASS_EXPIRED = new PlayFail(0x11);
	public static final PlayFail REASON_GAME_TIME_EXPIRED = new PlayFail(0x12);
	public static final PlayFail REASON_NO_TIME_LEFT = new PlayFail(0x13);
	public static final PlayFail REASON_SYSTEM_ERROR = new PlayFail(0x14);
	public static final PlayFail REASON_ACCESS_FAILED = new PlayFail(0x15);
	public static final PlayFail REASON_RESTRICTED_IP = new PlayFail(0x16);
	public static final PlayFail REASON_WEEK_USAGE_FINISHED = new PlayFail(0x1E);
	public static final PlayFail REASON_SECURITY_CARD_NUMBER_INVALID = new PlayFail(0x1F);
	public static final PlayFail REASON_AGE_NOT_VERIFIED_CANT_LOG_BEETWEEN_10PM_6AM = new PlayFail(0x20);
	public static final PlayFail REASON_SERVER_CANNOT_BE_ACCESSED_BY_YOUR_COUPON = new PlayFail(0x21);
	public static final PlayFail REASON_DUAL_BOX = new PlayFail(0x23);
	public static final PlayFail REASON_INACTIVE = new PlayFail(0x24);
	public static final PlayFail REASON_USER_AGREEMENT_REJECTED_ON_WEBSITE = new PlayFail(0x25);
	public static final PlayFail REASON_GUARDIAN_CONSENT_REQUIRED = new PlayFail(0x26);
	public static final PlayFail REASON_USER_AGREEMENT_DECLINED_OR_WITHDRAWL_REQUEST = new PlayFail(0x27);
	public static final PlayFail REASON_ACCOUNT_SUSPENDED_CALL = new PlayFail(0x28);
	public static final PlayFail REASON_CHANGE_PASSWORD_AND_QUIZ_ON_WEBSITE = new PlayFail(0x29);
	public static final PlayFail REASON_ALREADY_LOGGED_INTO_10_ACCOUNTS = new PlayFail(0x2A);
	public static final PlayFail REASON_MASTER_ACCOUNT_RESTRICTED = new PlayFail(0x2B);
	public static final PlayFail REASON_CERTIFICATION_FAILED = new PlayFail(0x2E);
	public static final PlayFail REASON_TELEPHONE_CERTIFICATION_UNAVAILABLE = new PlayFail(0x2F);
	public static final PlayFail REASON_TELEPHONE_SIGNALS_DELAYED = new PlayFail(0x30);
	public static final PlayFail REASON_CERTIFICATION_FAILED_LINE_BUSY = new PlayFail(0x31);
	public static final PlayFail REASON_CERTIFICATION_SERVICE_NUMBER_EXPIRED_OR_INCORRECT = new PlayFail(0x32);
	public static final PlayFail REASON_CERTIFICATION_SERVICE_CURRENTLY_BEING_CHECKED = new PlayFail(0x33);
	public static final PlayFail REASON_CERTIFICATION_SERVICE_CANT_BE_USED_HEAVY_VOLUME = new PlayFail(0x34);
	public static final PlayFail REASON_CERTIFICATION_SERVICE_EXPIRED_GAMEPLAY_BLOCKED = new PlayFail(0x35);
	public static final PlayFail REASON_CERTIFICATION_FAILED_3_TIMES_GAMEPLAY_BLOCKED_30_MIN = new PlayFail(0x36);
	public static final PlayFail REASON_CERTIFICATION_DAILY_USE_EXCEEDED = new PlayFail(0x37);
	public static final PlayFail REASON_CERTIFICATION_UNDERWAY_TRY_AGAIN_LATER = new PlayFail(0x38);

	private int reason;

	private PlayFail(int reason)
	{
		this.reason = reason;
	}

	/**
	 * @see l2s.commons.net.nio.impl.SendablePacket#write()
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x06);
		writeC(reason);
	}
}
