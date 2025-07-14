package l2s.gameserver.network.l2;

/**
 * @version - 507 protocol
 **/
public enum ServerPacketOpcodes507 implements ServerPacketOpcodes
{
	/*0x00*/ DiePacket,
	/*0x01*/ RevivePacket,
	/*0x02*/ AttackOutofRangePacket,
	/*0x03*/ AttackinCoolTimePacket,
	/*0x04*/ AttackDeadTargetPacket,
	/*0x05*/ SpawnItemPacket,
	/*0x06*/ SellListPacket,
	/*0x07*/ BuyListPacket,
	/*0x08*/ DeleteObjectPacket,
	/*0x09*/ CharacterSelectionInfoPacket,
	/*0x0A*/ LoginResultPacket,
	/*0x0B*/ CharacterSelectedPacket,
	/*0x0C*/ NpcInfoPacket,
	/*0x0D*/ NewCharacterSuccessPacket,
	/*0x0E*/ NewCharacterFailPacket,
	/*0x0F*/ CharacterCreateSuccessPacket,
	/*0x10*/ CharacterCreateFailPacket,
	/*0x11*/ ItemListPacket,
	/*0x12*/ SunRisePacket,
	/*0x13*/ SunSetPacket,
	/*0x14*/ TradeStartPacket,
	/*0x15*/ TradeStartOkPacket,
	/*0x16*/ DropItemPacket,
	/*0x17*/ GetItemPacket,
	/*0x18*/ StatusUpdatePacket,
	/*0x19*/ NpcHtmlMessagePacket,
	/*0x1A*/ TradeOwnAddPacket,
	/*0x1B*/ TradeOtherAddPacket,
	/*0x1C*/ TradeDonePacket,
	/*0x1D*/ CharacterDeleteSuccessPacket,
	/*0x1E*/ CharacterDeleteFailPacket,
	/*0x1F*/ ActionFailPacket,
	/*0x20*/ SeverClosePacket,
	/*0x21*/ InventoryUpdatePacket,
	/*0x22*/ TeleportToLocationPacket,
	/*0x23*/ TargetSelectedPacket,
	/*0x24*/ TargetUnselectedPacket,
	/*0x25*/ AutoAttackStartPacket,
	/*0x26*/ AutoAttackStopPacket,
	/*0x27*/ SocialActionPacket,
	/*0x28*/ ChangeMoveTypePacket,
	/*0x29*/ ChangeWaitTypePacket,
	/*0x2A*/ ManagePledgePowerPacket,
	/*0x2B*/ CreatePledgePacket,
	/*0x2C*/ AskJoinPledgePacket,
	/*0x2D*/ JoinPledgePacket,
	/*0x2E*/ VersionCheckPacket,
	/*0x2F*/ MTLPacket,
	/*0x30*/ NSPacket,
	/*0x31*/ CharInfo,
	/*0x32*/ UserInfo,
	/*0x33*/ AttackPacket,
	/*0x34*/ WithdrawalPledgePacket,
	/*0x35*/ OustPledgeMemberPacket,
	/*0x36*/ SetOustPledgeMemberPacket,
	/*0x37*/ DismissPledgePacket,
	/*0x38*/ SetDismissPledgePacket,
	/*0x39*/ AskJoinPartyPacket,
	/*0x3A*/ JoinPartyPacket,
	/*0x3B*/ WithdrawalPartyPacket,
	/*0x3C*/ OustPartyMemberPacket,
	/*0x3D*/ SetOustPartyMemberPacket,
	/*0x3E*/ DismissPartyPacket,
	/*0x3F*/ SetDismissPartyPacket,
	/*0x40*/ MagicAndSkillList,
	/*0x41*/ WareHouseDepositListPacket,
	/*0x42*/ WareHouseWithdrawListPacket,
	/*0x43*/ WareHouseDonePacket,
	/*0x44*/ ShortCutRegisterPacket,
	/*0x45*/ ShortCutInitPacket,
	/*0x46*/ ShortCutDeletePacket,
	/*0x47*/ StopMovePacket,
	/*0x48*/ MagicSkillUse,
	/*0x49*/ MagicSkillCanceled,
	/*0x4A*/ SayPacket2,
	/*0x4B*/ NpcInfoAbnormalVisualEffect,
	/*0x4C*/ DoorInfoPacket,
	/*0x4D*/ DoorStatusUpdatePacket,
	/*0x4E*/ PartySmallWindowAllPacket,
	/*0x4F*/ PartySmallWindowAddPacket,
	/*0x50*/ PartySmallWindowDeleteAllPacket,
	/*0x51*/ PartySmallWindowDeletePacket,
	/*0x52*/ PartySmallWindowUpdatePacket,
	/*0x53*/ TradePressOwnOkPacket,
	/*0x54*/ MagicSkillLaunchedPacket,
	/*0x55*/ FriendAddRequestResult,
	/*0x56*/ FriendAdd,
	/*0x57*/ FriendRemove,
	/*0x58*/ FriendList,
	/*0x59*/ FriendStatus,
	/*0x5A*/ PledgeShowMemberListAllPacket,
	/*0x5B*/ PledgeShowMemberListUpdatePacket,
	/*0x5C*/ PledgeShowMemberListAddPacket,
	/*0x5D*/ PledgeShowMemberListDeletePacket,
	/*0x5E*/ MagicListPacket,
	/*0x5F*/ SkillListPacket,
	/*0x60*/ VehicleInfoPacket,
	/*0x61*/ FinishRotatingPacket,
	/*0x62*/ SystemMessagePacket,
	/*0x63*/ StartPledgeWarPacket,
	/*0x64*/ ReplyStartPledgeWarPacket,
	/*0x65*/ StopPledgeWarPacket,
	/*0x66*/ ReplyStopPledgeWarPacket,
	/*0x67*/ SurrenderPledgeWarPacket,
	/*0x68*/ ReplySurrenderPledgeWarPacket,
	/*0x69*/ SetPledgeCrestPacket,
	/*0x6A*/ PledgeCrestPacket,
	/*0x6B*/ SetupGaugePacket,
	/*0x6C*/ VehicleDeparturePacket,
	/*0x6D*/ VehicleCheckLocationPacket,
	/*0x6E*/ GetOnVehiclePacket,
	/*0x6F*/ GetOffVehiclePacket,
	/*0x70*/ TradeRequestPacket,
	/*0x71*/ RestartResponsePacket,
	/*0x72*/ MoveToPawnPacket,
	/*0x73*/ SSQInfoPacket,
	/*0x74*/ GameGuardQueryPacket,
	/*0x75*/ L2FriendListPacket,
	/*0x76*/ L2FriendPacket,
	/*0x77*/ L2FriendStatusPacket,
	/*0x78*/ L2FriendSayPacket,
	/*0x79*/ ValidateLocationPacket,
	/*0x7A*/ StartRotatingPacket,
	/*0x7B*/ ShowBoardPacket,
	/*0x7C*/ ChooseInventoryItemPacket,
	/*0x7D*/ DummyPacket1,
	/*0x7E*/ MoveToLocationInVehiclePacket,
	/*0x7F*/ StopMoveInVehiclePacket,
	/*0x80*/ ValidateLocationInVehiclePacket,
	/*0x81*/ TradeUpdatePacket,
	/*0x82*/ TradePressOtherOkPacket,
	/*0x83*/ FriendAddRequest,
	/*0x84*/ LogOutOkPacket,
	/*0x85*/ AbnormalStatusUpdatePacket,
	/*0x86*/ QuestListPacket,
	/*0x87*/ EnchantResult,
	/*0x88*/ PledgeShowMemberListDeleteAllPacket,
	/*0x89*/ PledgeInfoPacket,
	/*0x8A*/ PledgeExtendedInfoPacket,
	/*0x8B*/ SummonInfoPacket,
	/*0x8C*/ RidePacket,
	/*0x8D*/ DummyPacket2,
	/*0x8E*/ PledgeShowInfoUpdatePacket,
	/*0x8F*/ ClientActionPacket,
	/*0x90*/ AcquireSkillListPacket,
	/*0x91*/ AcquireSkillInfoPacket,
	/*0x92*/ ServerObjectInfoPacket,
	/*0x93*/ GMHidePacket,
	/*0x94*/ AcquireSkillDonePacket,
	/*0x95*/ GMViewCharacterInfoPacket,
	/*0x96*/ GMViewPledgeInfoPacket,
	/*0x97*/ GMViewSkillInfoPacket,
	/*0x98*/ GMViewMagicInfoPacket,
	/*0x99*/ GMViewQuestInfoPacket,
	/*0x9A*/ GMViewItemListPacket,
	/*0x9B*/ GMViewWarehouseWithdrawListPacket,
	/*0x9C*/ ListPartyWaitingPacket,
	/*0x9D*/ PartyRoomInfoPacket,
	/*0x9E*/ PlaySoundPacket,
	/*0x9F*/ StaticObjectPacket,
	/*0xA0*/ PrivateStoreManageList,
	/*0xA1*/ PrivateStoreList,
	/*0xA2*/ PrivateStoreMsg,
	/*0xA3*/ ShowMinimapPacket,
	/*0xA4*/ ReviveRequestPacket,
	/*0xA5*/ AbnormalVisualEffectPacket,
	/*0xA6*/ TutorialShowHtmlPacket,
	/*0xA7*/ ShowTutorialMarkPacket,
	/*0xA8*/ TutorialEnableClientEventPacket,
	/*0xA9*/ TutorialCloseHtmlPacket,
	/*0xAA*/ ShowRadarPacket,
	/*0xAB*/ WithdrawAlliancePacket,
	/*0xAC*/ OustAllianceMemberPledgePacket,
	/*0xAD*/ DismissAlliancePacket,
	/*0xAE*/ SetAllianceCrestPacket,
	/*0xAF*/ AllianceCrestPacket,
	/*0xB0*/ ServerCloseSocketPacket,
	/*0xB1*/ PetStatusShowPacket,
	/*0xB2*/ MyPetSummonInfoPacket,
	/*0xB3*/ PetItemListPacket,
	/*0xB4*/ PetInventoryUpdatePacket,
	/*0xB5*/ AllianceInfoPacket,
	/*0xB6*/ PetStatusUpdatePacket,
	/*0xB7*/ PetDeletePacket,
	/*0xB8*/ DeleteRadarPacket,
	/*0xB9*/ MyTargetSelectedPacket,
	/*0xBA*/ PartyMemberPositionPacket,
	/*0xBB*/ AskJoinAlliancePacket,
	/*0xBC*/ JoinAlliancePacket,
	/*0xBD*/ PrivateStoreBuyManageList,
	/*0xBE*/ PrivateStoreBuyList,
	/*0xBF*/ PrivateStoreBuyMsg,
	/*0xC0*/ VehicleStartPacket,
	/*0xC1*/ NpcInfoState,
	/*0xC2*/ StartAllianceWarPacket,
	/*0xC3*/ ReplyStartAllianceWarPacket,
	/*0xC4*/ StopAllianceWarPacket,
	/*0xC5*/ ReplyStopAllianceWarPacket,
	/*0xC6*/ SurrenderAllianceWarPacket,
	/*0xC7*/ SkillCoolTimePacket,
	/*0xC8*/ PackageToListPacket,
	/*0xC9*/ CastleSiegeInfoPacket,
	/*0xCA*/ CastleSiegeAttackerListPacket,
	/*0xCB*/ CastleSiegeDefenderListPacket,
	/*0xCC*/ NickNameChangedPacket,
	/*0xCD*/ PledgeStatusChangedPacket,
	/*0xCE*/ RelationChangedPacket,
	/*0xCF*/ EventTriggerPacket,
	/*0xD0*/ MultiSellListPacket,
	/*0xD1*/ SetSummonRemainTimePacket,
	/*0xD2*/ PackageSendableListPacket,
	/*0xD3*/ EarthQuakePacket,
	/*0xD4*/ FlyToLocationPacket,
	/*0xD5*/ BlockListPacket,
	/*0xD6*/ SpecialCameraPacket,
	/*0xD7*/ NormalCameraPacket,
	/*0xD8*/ SkillRemainSecPacket,
	/*0xD9*/ NetPingPacket,
	/*0xDA*/ DicePacket,
	/*0xDB*/ SnoopPacket,
	/*0xDC*/ RecipeBookItemListPacket,
	/*0xDD*/ RecipeItemMakeInfoPacket,
	/*0xDE*/ RecipeShopManageListPacket,
	/*0xDF*/ RecipeShopSellListPacket,
	/*0xE0*/ RecipeShopItemInfoPacket,
	/*0xE1*/ RecipeShopMsgPacket,
	/*0xE2*/ ShowCalcPacket,
	/*0xE3*/ MonRaceInfoPacket,
	/*0xE4*/ HennaItemInfoPacket,
	/*0xE5*/ HennaInfoPacket,
	/*0xE6*/ HennaUnequipListPacket,
	/*0xE7*/ HennaUnequipInfoPacket,
	/*0xE8*/ MacroListPacket,
	/*0xE9*/ BuyListSeedPacket,
	/*0xEA*/ ShowTownMapPacket,
	/*0xEB*/ ObserverStartPacket,
	/*0xEC*/ ObserverEndPacket,
	/*0xED*/ ChairSitPacket,
	/*0xEE*/ HennaEquipListPacket,
	/*0xEF*/ SellListProcurePacket,
	/*0xF0*/ GMHennaInfoPacket,
	/*0xF1*/ RadarControlPacket,
	/*0xF2*/ ClientSetTimePacket,
	/*0xF3*/ ConfirmDlgPacket,
	/*0xF4*/ PartySpelledPacket,
	/*0xF5*/ ShopPreviewListPacket,
	/*0xF6*/ ShopPreviewInfoPacket,
	/*0xF7*/ CameraModePacket,
	/*0xF8*/ ShowXMasSealPacket,
	/*0xF9*/ EtcStatusUpdatePacket,
	/*0xFA*/ ShortBuffStatusUpdatePacket,
	/*0xFB*/ SSQStatusPacket,
	/*0xFC*/ PetitionVotePacket,
	/*0xFD*/ AgitDecoInfoPacket,
	/*0xFE:0x00*/ ExDummyPacket1,
	/*0xFE:0x01*/ ExRegenMaxPacket,
	/*0xFE:0x02*/ ExEventMatchUserInfoPacket,
	/*0xFE:0x03*/ ExColosseumFenceInfoPacket,
	/*0xFE:0x04*/ ExEventMatchSpelledInfoPacket,
	/*0xFE:0x05*/ ExEventMatchFirecrackerPacket,
	/*0xFE:0x06*/ ExEventMatchTeamUnlockedPacket,
	/*0xFE:0x07*/ ExEventMatchGMTestPacket,
	/*0xFE:0x08*/ ExPartyRoomMemberPacket,
	/*0xFE:0x09*/ ExClosePartyRoomPacket,
	/*0xFE:0x0A*/ ExManagePartyRoomMemberPacket,
	/*0xFE:0x0B*/ ExEventMatchLockResult,
	/*0xFE:0x0C*/ ExAutoSoulShot,
	/*0xFE:0x0D*/ ExEventMatchListPacket,
	/*0xFE:0x0E*/ ExEventMatchObserverPacket,
	/*0xFE:0x0F*/ ExEventMatchMessagePacket,
	/*0xFE:0x10*/ ExEventMatchScorePacket,
	/*0xFE:0x11*/ ExServerPrimitivePacket,
	/*0xFE:0x12*/ ExOpenMPCCPacket,
	/*0xFE:0x13*/ ExCloseMPCCPacket,
	/*0xFE:0x14*/ ExShowCastleInfo,
	/*0xFE:0x15*/ ExShowFortressInfo,
	/*0xFE:0x16*/ ExShowAgitInfo,
	/*0xFE:0x17*/ ExShowFortressSiegeInfo,
	/*0xFE:0x18*/ ExPartyPetWindowAdd,
	/*0xFE:0x19*/ ExPartyPetWindowUpdate,
	/*0xFE:0x1A*/ ExAskJoinMPCCPacket,
	/*0xFE:0x1B*/ ExPledgeEmblem,
	/*0xFE:0x1C*/ ExEventMatchTeamInfoPacket,
	/*0xFE:0x1D*/ ExEventMatchCreatePacket,
	/*0xFE:0x1E*/ ExFishingStartPacket,
	/*0xFE:0x1F*/ ExFishingEndPacket,
	/*0xFE:0x20*/ ExShowQuestInfoPacket,
	/*0xFE:0x21*/ ExShowQuestMarkPacket,
	/*0xFE:0x22*/ ExSendManorListPacket,
	/*0xFE:0x23*/ ExShowSeedInfoPacket,
	/*0xFE:0x24*/ ExShowCropInfoPacket,
	/*0xFE:0x25*/ ExShowManorDefaultInfoPacket,
	/*0xFE:0x26*/ ExShowSeedSettingPacket,
	/*0xFE:0x27*/ ExFishingStartCombatPacket,
	/*0xFE:0x28*/ ExFishingHpRegenPacket,
	/*0xFE:0x29*/ ExEnchantSkillListPacket,
	/*0xFE:0x2A*/ ExEnchantSkillInfoPacket,
	/*0xFE:0x2B*/ ExShowCropSettingPacket,
	/*0xFE:0x2C*/ ExShowSellCropListPacket,
	/*0xFE:0x2D*/ ExOlympiadMatchEndPacket,
	/*0xFE:0x2E*/ ExMailArrivedPacket,
	/*0xFE:0x2F*/ ExStorageMaxCountPacket,
	/*0xFE:0x30*/ ExEventMatchManagePacket,
	/*0xFE:0x31*/ ExMultiPartyCommandChannelInfoPacket,
	/*0xFE:0x32*/ ExPCCafePointInfoPacket,
	/*0xFE:0x33*/ ExSetCompassZoneCode,
	/*0xFE:0x34*/ ExGetBossRecord,
	/*0xFE:0x35*/ ExAskJoinPartyRoom,
	/*0xFE:0x36*/ ExListPartyMatchingWaitingRoom,
	/*0xFE:0x37*/ ExSetMpccRouting,
	/*0xFE:0x38*/ ExShowAdventurerGuideBook,
	/*0xFE:0x39*/ ExShowScreenMessage,
	/*0xFE:0x3A*/ PledgeSkillListPacket,
	/*0xFE:0x3B*/ PledgeSkillListAddPacket,
	/*0xFE:0x3C*/ PledgeSkillListRemovePacket,
	/*0xFE:0x3D*/ PledgePowerGradeList,
	/*0xFE:0x3E*/ PledgeReceivePowerInfo,
	/*0xFE:0x3F*/ PledgeReceiveMemberInfo,
	/*0xFE:0x40*/ PledgeReceiveWarList,
	/*0xFE:0x41*/ PledgeReceiveSubPledgeCreated,
	/*0xFE:0x42*/ ExRedSkyPacket,
	/*0xFE:0x43*/ PledgeReceiveUpdatePower,
	/*0xFE:0x44*/ FlySelfDestinationPacket,
	/*0xFE:0x45*/ ShowPCCafeCouponShowUI,
	/*0xFE:0x46*/ ExSearchOrc,
	/*0xFE:0x47*/ ExCursedWeaponList,
	/*0xFE:0x48*/ ExCursedWeaponLocation,
	/*0xFE:0x49*/ ExRestartClient,
	/*0xFE:0x4A*/ ExRequestHackShield,
	/*0xFE:0x4B*/ ExUseSharedGroupItem,
	/*0xFE:0x4C*/ ExMPCCShowPartyMemberInfo,
	/*0xFE:0x4D*/ ExDuelAskStart,
	/*0xFE:0x4E*/ ExDuelReady,
	/*0xFE:0x4F*/ ExDuelStart,
	/*0xFE:0x50*/ ExDuelEnd,
	/*0xFE:0x51*/ ExDuelUpdateUserInfo,
	/*0xFE:0x52*/ ExShowVariationMakeWindow,
	/*0xFE:0x53*/ ExShowVariationCancelWindow,
	/*0xFE:0x54*/ ExPutItemResultForVariationMake,
	/*0xFE:0x55*/ ExPutIntensiveResultForVariationMake,
	/*0xFE:0x56*/ ExPutCommissionResultForVariationMake,
	/*0xFE:0x57*/ ExVariationResult,
	/*0xFE:0x58*/ ExPutItemResultForVariationCancel,
	/*0xFE:0x59*/ ExVariationCancelResult,
	/*0xFE:0x5A*/ ExDuelEnemyRelation,
	/*0xFE:0x5B*/ ExPlayAnimation,
	/*0xFE:0x5C*/ ExMPCCPartyInfoUpdate,
	/*0xFE:0x5D*/ ExPlayScene,
	/*0xFE:0x5E*/ ExSpawnEmitterPacket,
	/*0xFE:0x5F*/ ExEnchantSkillInfoDetailPacket,
	/*0xFE:0x60*/ ExBasicActionList,
	/*0xFE:0x61*/ ExAirShipInfo,
	/*0xFE:0x62*/ ExAttributeEnchantResultPacket,
	/*0xFE:0x63*/ ExChooseInventoryAttributeItemPacket,
	/*0xFE:0x64*/ ExGetOnAirShipPacket,
	/*0xFE:0x65*/ ExGetOffAirShipPacket,
	/*0xFE:0x66*/ ExMoveToLocationAirShipPacket,
	/*0xFE:0x67*/ ExStopMoveAirShipPacket,
	/*0xFE:0x68*/ ExShowTracePacket,
	/*0xFE:0x69*/ ExItemAuctionInfoPacket,
	/*0xFE:0x6A*/ ExNeedToChangeName,
	/*0xFE:0x6B*/ ExPartyPetWindowDelete,
	/*0xFE:0x6C*/ ExTutorialList,
	/*0xFE:0x6D*/ ExRpItemLink,
	/*0xFE:0x6E*/ ExMoveToLocationInAirShipPacket,
	/*0xFE:0x6F*/ ExStopMoveInAirShipPacket,
	/*0xFE:0x70*/ ExValidateLocationInAirShipPacket,
	/*0xFE:0x71*/ ExUISettingPacket,
	/*0xFE:0x72*/ ExMoveToTargetInAirShipPacket,
	/*0xFE:0x73*/ ExAttackInAirShipPacket,
	/*0xFE:0x74*/ ExMagicSkillUseInAirShipPacket,
	/*0xFE:0x75*/ ExShowBaseAttributeCancelWindow,
	/*0xFE:0x76*/ ExBaseAttributeCancelResult,
	/*0xFE:0x77*/ ExSubPledgetSkillAdd,
	/*0xFE:0x78*/ ExResponseFreeServer,
	/*0xFE:0x79*/ ExShowProcureCropDetailPacket,
	/*0xFE:0x7A*/ ExHeroListPacket,
	/*0xFE:0x7B*/ ExOlympiadUserInfoPacket,
	/*0xFE:0x7C*/ ExOlympiadSpelledInfoPacket,
	/*0xFE:0x7D*/ ExOlympiadModePacket,
	/*0xFE:0x7E*/ ExShowFortressMapInfo,
	/*0xFE:0x7F*/ ExPVPMatchRecord,
	/*0xFE:0x80*/ ExPVPMatchUserDie,
	/*0xFE:0x81*/ ExPrivateStoreWholeMsg,
	/*0xFE:0x82*/ ExPutEnchantTargetItemResult,
	/*0xFE:0x83*/ ExPutEnchantSupportItemResult,
	/*0xFE:0x84*/ ExChangeNicknameNColor,
	/*0xFE:0x85*/ ExGetBookMarkInfoPacket,
	/*0xFE:0x86*/ ExNotifyPremiumItem,
	/*0xFE:0x87*/ ExGetPremiumItemListPacket,
	/*0xFE:0x88*/ ExPeriodicItemList,
	/*0xFE:0x89*/ ExJumpToLocation,
	/*0xFE:0x8A*/ ExPVPMatchCCRecord,
	/*0xFE:0x8B*/ ExPVPMatchCCMyRecord,
	/*0xFE:0x8C*/ ExPVPMatchCCRetire,
	/*0xFE:0x8D*/ ExShowTerritory,
	/*0xFE:0x8E*/ ExNpcQuestHtmlMessage,
	/*0xFE:0x8F*/ ExSendUIEventPacket,
	/*0xFE:0x90*/ ExNotifyBirthDay,
	/*0xFE:0x91*/ ExShowDominionRegistry,
	/*0xFE:0x92*/ ExReplyRegisterDominion,
	/*0xFE:0x93*/ ExReplyDominionInfo,
	/*0xFE:0x94*/ ExShowOwnthingPos,
	/*0xFE:0x95*/ ExCleftList,
	/*0xFE:0x96*/ ExCleftState,
	/*0xFE:0x97*/ ExDominionChannelSet,
	/*0xFE:0x98*/ ExBlockUpSetList,
	/*0xFE:0x99*/ ExBlockUpSetState,
	/*0xFE:0x9A*/ ExStartScenePlayer,
	/*0xFE:0x9B*/ ExAirShipTeleportList,
	/*0xFE:0x9C*/ ExMpccRoomInfo,
	/*0xFE:0x9D*/ ExListMpccWaiting,
	/*0xFE:0x9E*/ ExDissmissMpccRoom,
	/*0xFE:0x9F*/ ExManageMpccRoomMember,
	/*0xFE:0xA0*/ ExMpccRoomMember,
	/*0xFE:0xA1*/ ExVitalityPointInfo,
	/*0xFE:0xA2*/ ExShowSeedMapInfo,
	/*0xFE:0xA3*/ ExMpccPartymasterList,
	/*0xFE:0xA4*/ ExDominionWarStart,
	/*0xFE:0xA5*/ ExDominionWarEnd,
	/*0xFE:0xA6*/ ExShowLines,
	/*0xFE:0xA7*/ ExPartyMemberRenamed,
	/*0xFE:0xA8*/ ExEnchantSkillResult,
	/*0xFE:0xA9*/ ExRefundList,
	/*0xFE:0xAA*/ ExNoticePostArrived,
	/*0xFE:0xAB*/ ExShowReceivedPostList,
	/*0xFE:0xAC*/ ExReplyReceivedPost,
	/*0xFE:0xAD*/ ExShowSentPostList,
	/*0xFE:0xAE*/ ExReplySentPost,
	/*0xFE:0xAF*/ ExResponseShowStepOne,
	/*0xFE:0xB0*/ ExResponseShowStepTwo,
	/*0xFE:0xB1*/ ExResponseShowContents,
	/*0xFE:0xB2*/ ExShowPetitionHtml,
	/*0xFE:0xB3*/ ExReplyPostItemList,
	/*0xFE:0xB4*/ ExChangePostState,
	/*0xFE:0xB5*/ ExReplyWritePost,
	ExPostItemFee,
	/*0xFE:0xB6*/ ExInitializeSeed,
	/*0xFE:0xB7*/ ExRaidReserveResult,
	/*0xFE:0xB8*/ ExBuySellListPacket,
	/*0xFE:0xB9*/ ExCloseRaidSocket,
	/*0xFE:0xBA*/ ExPrivateMarketListPacket,
	/*0xFE:0xBB*/ ExRaidCharacterSelected,
	/*0xFE:0xBC*/ ExAskCoupleAction,
	/*0xFE:0xBD*/ ExBrBroadcastEventState,
	/*0xFE:0xBE*/ ExBR_LoadEventTopRankersPacket,
	/*0xFE:0xBF*/ ExChangeNPCState,
	/*0xFE:0xC0*/ ExAskModifyPartyLooting,
	/*0xFE:0xC1*/ ExSetPartyLooting,
	/*0xFE:0xC2*/ ExRotation,
	/*0xFE:0xC3*/ ExChangeClientEffectInfo,
	/*0xFE:0xC4*/ ExMembershipInfo,
	/*0xFE:0xC5*/ ExReplyHandOverPartyMaster,
	/*0xFE:0xC6*/ ExQuestNpcLogList,
	/*0xFE:0xC7*/ ExQuestItemListPacket,
	/*0xFE:0xC8*/ ExGMViewQuestItemListPacket,
	/*0xFE:0xC9*/ ExRestartResponse,
	/*0xFE:0xCA*/ ExVoteSystemInfoPacket,
	/*0xFE:0xCB*/ ExShuttleInfoPacket,
	/*0xFE:0xCC*/ ExSuttleGetOnPacket,
	/*0xFE:0xCD*/ ExSuttleGetOffPacket,
	/*0xFE:0xCE*/ ExSuttleMovePacket,
	/*0xFE:0xCF*/ ExMTLInSuttlePacket,
	/*0xFE:0xD0*/ ExStopMoveInShuttlePacket,
	/*0xFE:0xD1*/ ExValidateLocationInShuttlePacket,
	/*0xFE:0xD2*/ ExAgitAuctionCmdPacket,
	/*0xFE:0xD3*/ ExConfirmAddingPostFriend,
	/*0xFE:0xD4*/ ExReceiveShowPostFriend,
	/*0xFE:0xD5*/ ExReceiveOlympiadPacket, //S_EX_GFX_OLYMPIAD
	/*0xFE:0xD6*/ ExBR_GamePointPacket,
	/*0xFE:0xD7*/ ExBR_ProductListPacket,
	/*0xFE:0xD8*/ ExBR_ProductInfoPacket,
	/*0xFE:0xD9*/ ExBR_BuyProductPacket,
	/*0xFE:0xDA*/ ExBR_PremiumStatePacket,
	/*0xFE:0xDB*/ ExBrExtraUserInfo,
	/*0xFE:0xDC*/ ExBrBuffEventState,
	/*0xFE:0xDD*/ ExBR_RecentProductListPacket,
	/*0xFE:0xDE*/ ExBR_MinigameLoadScoresPacket,
	/*0xFE:0xDF*/ ExBR_AgathionEnergyInfoPacket,
	/*0xFE:0xE0*/ ExShowChannelingEffectPacket,
	/*0xFE:0xE1*/ ExGetCrystalizingEstimation,
	/*0xFE:0xE2*/ ExGetCrystalizingFail,
	/*0xFE:0xE3*/ ExNavitAdventPointInfoPacket,
	/*0xFE:0xE4*/ ExNavitAdventEffectPacket,
	/*0xFE:0xE5*/ ExNavitAdventTimeChangePacket,
	/*0xFE:0xE6*/ ExAbnormalStatusUpdateFromTargetPacket,
	/*0xFE:0xE7*/ ExStopScenePlayerPacket,
	/*0xFE:0xE8*/ ExFlyMove,
	/*0xFE:0xE9*/ ExDynamicQuestPacket,
	/*0xFE:0xEA*/ ExSubjobInfo,
	/*0xFE:0xEB*/ ExChangeMPCost,
	/*0xFE:0xEC*/ ExFriendDetailInfo,
	/*0xFE:0xED*/ ExBlockAddResult,
	/*0xFE:0xEE*/ ExBlockRemoveResult,
	/*0xFE:0xEF*/ ExBlockDefailInfo,
	/*0xFE:0xF0*/ ExLoadInzonePartyHistory,
	/*0xFE:0xF1*/ ExFriendNotifyNameChange,
	/*0xFE:0xF2*/ ExShowCommission,
	/*0xFE:0xF3*/ ExResponseCommissionItemList,
	/*0xFE:0xF4*/ ExResponseCommissionInfo,
	/*0xFE:0xF5*/ ExResponseCommissionRegister,
	/*0xFE:0xF6*/ ExResponseCommissionDelete,
	/*0xFE:0xF7*/ ExResponseCommissionList,
	/*0xFE:0xF8*/ ExResponseCommissionBuyInfo,
	/*0xFE:0xF9*/ ExResponseCommissionBuyItem,
	/*0xFE:0xFA*/ ExAcquirableSkillListByClass,
	/*0xFE:0xFB*/ ExMagicAttackInfo,
	/*0xFE:0xFC*/ ExAcquireSkillInfo,
	/*0xFE:0xFD*/ ExNewSkillToLearnByLevelUp,
	/*0xFE:0xFE*/ ExCallToChangeClass,
	/*0xFE:0xFF*/ ExChangeToAwakenedClass,
	/*0xFE:0x100*/ ExTacticalSign,
	/*0xFE:0x101*/ ExLoadStatWorldRank,
	/*0xFE:0x102*/ ExLoadStatUser,
	/*0xFE:0x103*/ ExLoadStatHotLink,
	/*0xFE:0x104*/ ExGetWebSessionID,
	/*0xFE:0x105*/ Ex2NDPasswordCheckPacket,
	/*0xFE:0x106*/ Ex2NDPasswordVerifyPacket,
	/*0xFE:0x107*/ Ex2NDPasswordAckPacket,
	/*0xFE:0x108*/ ExFlyMoveBroadcast,
	/*0xFE:0x109*/ ExShowUsmPacket,
	/*0xFE:0x10A*/ ExShowStatPage,
	/*0xFE:0x10B*/ ExIsCharNameCreatable,
	/*0xFE:0x10C*/ ExGoodsInventoryChangedNotify,
	/*0xFE:0x10D*/ ExGoodsInventoryInfo,
	/*0xFE:0x10E*/ ExGoodsInventoryResult,
	/*0xFE:0x10F*/ ExAlterSkillRequest,
	/*0xFE:0x110*/ ExNotifyFlyMoveStart,
	/*0xFE:0x111*/ ExDummyPacket2,
	/*0xFE:0x112*/ ExCloseCommission,
	/*0xFE:0x113*/ ExChangeAttributeItemList,
	/*0xFE:0x114*/ ExChangeAttributeInfo,
	/*0xFE:0x115*/ ExChangeAttributeOk,
	/*0xFE:0x116*/ ExChangeAttributeFail,
	/*0xFE:0x117*/ ExLightingCandleEvent,
	/*0xFE:0x118*/ ExVitalityEffectInfo,
	/*0xFE:0x119*/ ExLoginVitalityEffectInfo,
	/*0xFE:0x11A*/ ExBR_PresentBuyProductPacket,
	/*0xFE:0x11B*/ ExMentorList,
	/*0xFE:0x11C*/ ExMentorAdd,
	/*0xFE:0x11D*/ ListMenteeWaitingPacket,
	/*0xFE:0x11E*/ ExInzoneWaitingInfo,
	/*0xFE:0x11F*/ ExCuriousHouseState,
	/*0xFE:0x120*/ ExCuriousHouseEnter,
	/*0xFE:0x121*/ ExCuriousHouseLeave,
	/*0xFE:0x122*/ ExCuriousHouseMemberList,
	/*0xFE:0x123*/ ExCuriousHouseMemberUpdate,
	/*0xFE:0x124*/ ExCuriousHouseRemainTime,
	/*0xFE:0x125*/ ExCuriousHouseResult,
	/*0xFE:0x126*/ ExCuriousHouseObserveList,
	/*0xFE:0x127*/ ExCuriousHouseObserveMode,
	/*0xFE:0x128*/ ExSysstring,
	/*0xFE:0x129*/ ExChoose_Shape_Shifting_Item,
	/*0xFE:0x12A*/ ExPut_Shape_Shifting_Target_Item_Result,
	/*0xFE:0x12B*/ ExPut_Shape_Shifting_Extraction_Item_Result,
	/*0xFE:0x12C*/ ExShape_Shifting_Result,
	/*0xFE:0x12D*/ ExCastleState,
	/*0xFE:0x12E*/ ExNCGuardReceiveDataFromServer,
	/*0xFE:0x12F*/ ExKalieEvent,
	/*0xFE:0x130*/ ExKalieEventJackpotUser,
	/*0xFE:0x131*/ ExAbnormalVisualEffectInfo,
	/*0xFE:0x132*/ ExNpcInfoSpeed,
	/*0xFE:0x133*/ ExSetPledgeEmblemAck,
	/*0xFE:0x134*/ ExShowBeautyMenuPacket,
	/*0xFE:0x135*/ ExResponseBeautyListPacket,
	/*0xFE:0x136*/ ExResponseBeautyRegistResetPacket,
	/*0xFE:0x137*/ ExResponseResetListPacket,
	/*0xFE:0x138*/ ExShuffleSeedAndPublicKey,
	/*0xFE:0x139*/ ExCheck_SpeedHack,
	/*0xFE:0x13A*/ ExBR_NewIConCashBtnWnd,
	/*0xFE:0x13B*/ ExEvent_Campaign_Info,
	/*0xFE:0x13C*/ ExUnReadMailCount,
	/*0xFE:0x13D*/ ExPledgeCount,
	/*0xFE:0x13E*/ ExAdenaInvenCount,
	/*0xFE:0x13F*/ ExPledgeRecruitInfo,
	/*0xFE:0x140*/ ExPledgeRecruitApplyInfo,
	/*0xFE:0x141*/ ExPledgeRecruitBoardSearch,
	/*0xFE:0x142*/ ExPledgeRecruitBoardDetail,
	/*0xFE:0x143*/ ExPledgeWaitingListApplied,
	/*0xFE:0x144*/ ExPledgeWaitingList,
	/*0xFE:0x145*/ ExPledgeWaitingUser,
	/*0xFE:0x146*/ ExPledgeDraftListSearch,
	/*0xFE:0x147*/ ExPledgeWaitingListAlarm,
	/*0xFE:0x148*/ ExValidateActiveCharacter,
	/*0xFE:0x149*/ ExCloseCommissionRegister,
	/*0xFE:0x14A*/ ExTeleportToLocationActivate,
	/*0xFE:0x14B*/ ExNotifyWebPetitionReplyAlarm,
	/*0xFE:0x14C*/ ExEventShowXMasWishCard,
	/*0xFE:0x14D*/ ExInvitation_Event_UI_Setting,
	/*0xFE:0x14E*/ ExInvitation_Event_Ink_Energy,
	/*0xFE:0x14F*/ Ex_Check_Abusing,
	/*0xFE:0x150*/ ExGMVitalityEffectInfo,
	/*0xFE:0x151*/ ExPathToAwakeningAlarm,
	/*0xFE:0x152*/ ExPutEnchantScrollItemResult,
	/*0xFE:0x153*/ ExRemoveEnchantSupportItemResult,
	/*0xFE:0x154*/ ExShowCardRewardList,
	/*0xFE:0x155*/ ExGmViewCharacterInfo,
	/*0xFE:0x156*/ ExUserInfoEquipSlot,
	/*0xFE:0x157*/ ExUserInfoCubic,
	/*0xFE:0x158*/ ExUserInfoAbnormalVisualEffect,
	/*0xFE:0x159*/ ExUserInfoFishing,
	/*0xFE:0x15A*/ ExPartySpelledInfoUpdate,
	/*0xFE:0x15B*/ ExDivideAdenaStart,
	/*0xFE:0x15C*/ ExDivideAdenaCancel,
	/*0xFE:0x15D*/ ExDivideAdenaDone,
	/*0xFE:0x15E*/ PetInfoPacket,
	/*0xFE:0x15F*/ ExAcquireAPSkillList, //EX_ACQUIRE_AP_SKILL_LIST
	/*0xFE:0x160*/ ExStartLuckyGame,
	/*0xFE:0x161*/ ExBettingLuckyGameResult,
	/*0xFE:0x162*/ ExTrainingZone_Admission,
	/*0xFE:0x163*/ ExTrainingZone_Leaving,
	/*0xFE:0x164*/ ExPeriodicHenna,
	/*0xFE:0x165*/ ExShowAPListWnd,
	/*0xFE:0x166*/ ExUserInfoInvenWeight,
	/*0xFE:0x167*/ ExCloseAPListWnd,
	/*0xFE:0x168*/ ExEnchantOneOK,
	/*0xFE:0x169*/ ExEnchantOneFail,
	/*0xFE:0x16A*/ ExEnchantOneRemoveOK,
	/*0xFE:0x16B*/ ExEnchantOneRemoveFail,
	/*0xFE:0x16C*/ ExEnchantTwoOK,
	/*0xFE:0x16D*/ ExEnchantTwoFail,
	/*0xFE:0x16E*/ ExEnchantTwoRemoveOK,
	/*0xFE:0x16F*/ ExEnchantTwoRemoveFail,
	/*0xFE:0x170*/ ExEnchantSucess,
	/*0xFE:0x171*/ ExEnchantFail,
	/*0xFE:0x172*/ ExEnchantRetryToPutItemOk,
	/*0xFE:0x173*/ ExEnchantRetryToPutItemFail,
	/*0xFE:0x174*/ ExAccountAttendanceInfo,
	/*0xFE:0x175*/ ExWorldChatCnt,
	/*0xFE:0x176*/ ExAlchemySkillList,
	/*0xFE:0x177*/ ExTryMixCube,
	/*0xFE:0x178*/ ExAlchemyConversion,
	/*0xFE:0x179*/ ExBeautyItemList,
	/*0xFE:0x17A*/ ExReceiveClientINI,
	/*0xFE:0x17B*/ ExAutoFishAvailable,
	/*0xFE:0x17C*/ ExChannlChatEnterWorld,
	/*0xFE:0x17D*/ ExChannlChatPlegeInfo,
	/*0xFE:0x17E*/ ExVipAttendanceItemList,
	/*0xFE:0x17F*/ ExConfirmVipAttendanceCheck,
	/*0xFE:0x180*/ ExShowEnsoulWindow,
	/*0xFE:0x181*/ ExEnsoulResult,
	/*0xFE:0x182*/ ExMultiSellResult,
	/*0xFE:0x183*/ ExCastleWarSeasonResult,
	/*0xFE:0x184*/ ExCastleWarSeasonReward,
	/*0xFE:0x185*/ ReciveVipProductList,
	/*0xFE:0x186*/ ReciveVipLuckyGameInfo,
	/*0xFE:0x187*/ ReciveVipLuckyGameItemList,
	/*0xFE:0x188*/ ReciveVipLuckyGameResult,
	/*0xFE:0x189*/ ReciveVipInfo,
	/*0xFE:0x18A*/ ReciveVipInfoRemainTime,
	/*0xFE:0x18B*/ ReceiveVipBotCaptchaImage,
	/*0xFE:0x18C*/ ReceiveVipBotCaptchaAnswerResult,
	/*0xFE:0x18D*/ ExPledgeSigninForOpenJoiningMethod,
	/*0xFE:0x18E*/ ExRequestMatchArena,
	/*0xFE:0x18F*/ ExCompleteMatchArena,
	/*0xFE:0x190*/ ExConfirmMatchArena,
	/*0xFE:0x191*/ ExCancelMatchArena,
	/*0xFE:0x192*/ ExStartChooseClassArena,
	/*0xFE:0x193*/ ExChangeClassArena,
	/*0xFE:0x194*/ ExConfirmClassArena,
	/*0xFE:0x195*/ ExStartBattleReadyArena,
	/*0xFE:0x196*/ ExBattleReadyArena,
	/*0xFE:0x197*/ ExDecoNPCInfo,
	/*0xFE:0x198*/ ExDecoNPCSet,
	/*0xFE:0x199*/ ExFactionInfo,
	/*0xFE:0x19A*/ ExBattleResultArena,
	/*0xFE:0x19B*/ ExClosingArena,
	/*0xFE:0x19C*/ ExClosedArena,
	/*0xFE:0x19D*/ ExDieInArena,
	/*0xFE:0x19E*/ DummyPacket,
	/*0xFE:0x19F*/ ExArenaDashboard,
	/*0xFE:0x1A0*/ ExArenaUpdateEquipSlot,
	/*0xFE:0x1A1*/ ExArenaKillInfo,
	/*0xFE:0x1A2*/ ExExitArena,
	/*0xFE:0x1A3*/ ExBalthusEvent,
	/*0xFE:0x1A4*/ ExBalthusEventJackpotUser,
	/*0xFE:0x1A5*/ ExPartyMatchingRoomHistory,
	/*0xFE:0x1A6*/ ExAIContentUIEvent,
	/*0xFE:0x1A7*/ ExArenaCustomNotification,
	/*0xFE:0x1A8*/ ExOneDayRewardList,
	/*0xFE:0x1A9*/ ExOneDayRewardInfo,
	/*0xFE:0x1AA*/ ExTodoListRecommand, //S_EX_TODOLIST_RECOMMAND
	/*0xFE:0x1AB*/ ExTodoListInzone,
	/*0xFE:0x1AC*/ ExTodoListHTML,
	/*0xFE:0x1AD*/ ExQueueTicket,
	/*0xFE:0x1AE*/ ExPledgeBonusOpen,
	/*0xFE:0x1AF*/ ExPledgeBonusList,
	/*0xFE:0x1B0*/ ExPledgeBonusMarkReset,
	/*0xFE:0x1B1*/ ExPledgeBonusUpdate,
	/*0xFE:0x1B2*/ ExSSOAuthnToken,
	/*0xFE:0x1B3*/ ExQueueTicketLogin,
	/*0xFE:0x1B4*/ ExEnSoulExtractionShow,
	/*0xFE:0x1B5*/ ExEnSoulExtractionResult,
	/*0xFE:0x1B6*/ ExFieldEventStep,
	/*0xFE:0x1B7*/ ExFieldEventPoint,
	/*0xFE:0x1B8*/ ExFieldEventEffect,
	/*0xFE:0x1B9*/ ExRaidBossSpawnInfo,
	/*0xFE:0x1BA*/ ExRaidServerInfo,
	/*0xFE:0x1BB*/ ExShowAgitSiegeInfo,
	/*0xFE:0x1BC*/ ExItemAuctionStatus,
	/*0xFE:0x1BD*/ ExMonsterBook,
	/*0xFE:0x1BE*/ ExMonsterBookRewardIcon,
	/*0xFE:0x1BF*/ ExMonsterBookOnFactionUI,
	/*0xFE:0x1C0*/ ExMonsterBookOpenResult,
	/*0xFE:0x1C1*/ ExMonsterBookCloseForce,
	/*0xFE:0x1C2*/ ExFactionLevelUpNotify,
	/*0xFE:0x1C3*/ ExItemAuctionNextInfoPacket,
	/*0xFE:0x1C4*/ ExItemAuctionUpdatedBiddingInfoPacket,
	/*0xFE:0x1C5*/ ExPrivateStoreBuyingResult,
	/*0xFE:0x1C6*/ ExPrivateStoreSellingResult,
	/*0xFE:0x1C7*/ ExEnterWorldPacket,
	/*0xFE:0x1C8*/ ExMatchGroup,
	/*0xFE:0x1C9*/ ExMatchGroupAsk,
	/*0xFE:0x1CA*/ ExMatchGroupWithdraw,
	/*0xFE:0x1CB*/ ExMatchGroupOust,
	/*0xFE:0x1CC*/ ExArenaShowEnemyPartyLocation,
	/*0xFE:0x1CD*/ ExShowUpgradeSystem, // S_EX_SHOW_UPGRADE_SYSTEM
	/*0xFE:0x1CE*/ ExUpgradeSystemResult, // S_EX_UPGRADE_SYSTEM_RESULT
	/*0xFE:0x1CF*/ ExCardUpdownGameStart, // S_EX_CARD_UPDOWN_GAME_START
	/*0xFE:0x1D0*/ ExCardUpdownPickResult, // S_EX_CARD_UPDOWN_PICK_RESULT
	/*0xFE:0x1D1*/ ExCardUpdownPrepareReward, // S_EX_CARD_UPDOWN_GAME_PREPARE_REWARD
	/*0xFE:0x1D2*/ ExCardUpdownGameRewardReply, // S_EX_CARD_UPDOWN_GAME_REWARD_REPLY
	/*0xFE:0x1D3*/ ExCardUpdownGameQuit, // S_EX_CARD_UPDOWN_GAME_QUIT
	/*0xFE:0x1D4*/ ExArenaRankAll, // S_EX_ARENA_RANK_ALL
	/*0xFE:0x1D5*/ ExArenaMyrank, // S_EX_ARENA_MYRANK
	/*0xFE:0x1D6*/ ExPledgeClassicRaidInfo, // S_EX_PLEDGE_CLASSIC_RAID_INFO
	/*0xFE:0x1D7*/ ExArenaObserve, // S_EX_ARENA_OBSERVE
	/*0xFE:0x1D8*/ ExHtmlWithNpcViewport, // S_EX_HTML_WITH_NPC_VIEWPORT
	/*0xFE:0x1D9*/ ExPledgeContributionRank, // S_EX_PLEDGE_CONTRIBUTION_RANK ddd
	/*0xFE:0x1DA*/ ExPledgeContributionInfo, // S_EX_PLEDGE_CONTRIBUTION_INFO
	/*0xFE:0x1DB*/ ExPledgeContributionReward, // S_EX_PLEDGE_CONTRIBUTION_REWARD
	/*0xFE:0x1DC*/ ExPledgeRaidInfo, // S_EX_PLEDGE_RAID_INFO
	/*0xFE:0x1DD*/ ExPledgeRaidRank, // S_EX_PLEDGE_RAID_RANK
	/*0xFE:0x1DE*/ ExPledgeLevelUp, // S_EX_PLEDGE_LEVEL_UP
	/*0xFE:0x1DF*/ ExPledgeShowInfoUpdate, // S_EX_PLEDGE_SHOW_INFO_UPDATE
	/*0xFE:0x1E0*/ ExPledgeMissionInfo, // S_EX_PLEDGE_MISSION_INFO
	/*0xFE:0x1E1*/ ExPledgeMissionRewardCount, // S_EX_PLEDGE_MISSION_REWARD_COUNT
	/*0xFE:0x1E2*/ ExPledgeMasteryInfo, // S_EX_PLEDGE_MASTERY_INFO
	/*0xFE:0x1E3*/ ExPledgeMasterySet, // S_EX_PLEDGE_MASTERY_SET
	/*0xFE:0x1E4*/ ExPledgeMasteryReset, // S_EX_PLEDGE_MASTERY_RESET
	/*0xFE:0x1E5*/ ExTutorialShowId, // S_EX_TUTORIAL_SHOW_ID
	/*0xFE:0x1E6*/ ExPledgeSkillInfo, // S_EX_PLEDGE_SKILL_INFO
	/*0xFE:0x1E7*/ ExPledgeSkillActivate, // S_EX_PLEDGE_SKILL_ACTIVATE
	/*0xFE:0x1E8*/ ExPledgeItemList, // S_EX_PLEDGE_ITEM_LIST cQdccQdddhh
	/*0xFE:0x1E9*/ ExPledgeItemActivate, // S_EX_PLEDGE_ITEM_ACTIVATE
	/*0xFE:0x1EA*/ ExPledgeAnnounce, // S_EX_PLEDGE_ANNOUNCE
	/*0xFE:0x1EB*/ ExPledgeAnnounceSet, // S_EX_PLEDGE_ANNOUNCE_SET
	/*0xFE:0x1EC*/ ExSetPledgeEmblem, // S_EX_SET_PLEDGE_EMBLEM
	/*0xFE:0x1ED*/ ExShowCreatePledge, // S_EX_SHOW_CREATE_PLEDGE
	/*0xFE:0x1EE*/ ExPledgeItemInfo, // S_EX_PLEDGE_ITEM_INFO cQdccQdddhh
	/*0xFE:0x1EF*/ ExPledgeItemBuy, // S_EX_PLEDGE_ITEM_BUY
	/*0xFE:0x1F0*/ ExElementalSpiritInfo, // S_EX_ELEMENTAL_SPIRIT_INFO ccc cc cdQQQdddddddddddc hq dq
	/*0xFE:0x1F1*/ ExElementalSpiritExtractInfo, // S_EX_ELEMENTAL_SPIRIT_EXTRACT_INFO ccc dd dd
	/*0xFE:0x1F2*/ ExElementalSpiritEvolutionInfo, // S_EX_ELEMENTAL_SPIRIT_EVOLUTION_INFO cdd df ddd
	/*0xFE:0x1F3*/ ExElementalSpiritEvolution, // S_EX_ELEMENTAL_SPIRIT_EVOLUTION cc ccdQQQddddddddddd hq
	/*0xFE:0x1F4*/ ExElementalSpiritSetTalent, // S_EX_ELEMENTAL_SPIRIT_SET_TALENT cc ccdQQQddddddddddd hq
	/*0xFE:0x1F5*/ ExElementalSpiritAbsorbInfo, // S_EX_ELEMENTAL_SPIRIT_ABSORB_INFO cccQQQddd ddd
	/*0xFE:0x1F6*/ ExElementalSpiritAbsorb, // S_EX_ELEMENTAL_SPIRIT_ABSORB cc ccdQQQddddddddddd hq
	/*0xFE:0x1F7*/ ExChooseLockedItem, // S_EX_CHOOSE_LOCKED_ITEM
	/*0xFE:0x1F8*/ ExLockedResult, // S_EX_LOCKED_RESULT
	/*0xFE:0x1F9*/ ExElementalSpiritExtract, // S_EX_ELEMENTAL_SPIRIT_EXTRACT cc ccdQQQddddddddddd hq
	/*0xFE:0x1FA*/ ExOlympiadInfo, // S_EX_OLYMPIAD_INFO
	/*0xFE:0x1FB*/ ExOlympiadRecord, // S_EX_OLYMPIAD_RECORD
	/*0xFE:0x1FC*/ ExOlympiadMatchInfo, // S_EX_OLYMPIAD_MATCH_INFO
	/*0xFE:0x1FD*/ ExElementalSpiritGetExp, // S_EX_ELEMENTAL_SPIRIT_GET_EXP cQ
	/*0xFE:0x1FE*/ ExItemAnnounce, // S_EX_ITEM_ANNOUNCE
	/*0xFE:0x1FF*/ ExCompletedDailyQuestList, // S_EX_COMPLETED_DAILY_QUEST_LIST
	/*0xFE:0x200*/ ExCompletedDailyQuest, // S_EX_COMPLETED_DAILY_QUEST
	/*0xFE:0x201*/ ExUserBanInfo, // S_EX_USER_BAN_INFO
	/*0xFE:0x202*/ ExTryEnchantArtifactResult, // S_EX_TRY_ENCHANT_ARTIFACT_RESULT
	/*0xFE:0x203*/ ExShowUpgradeSystemNormal, // S_EX_SHOW_UPGRADE_SYSTEM_NORMAL
	/*0xFE:0x204*/ ExUpgradeSystemNormalResult, // S_EX_UPGRADE_SYSTEM_NORMAL_RESULT
	/*0xFE:0x205*/ ExPurchaseLimitShopItemList, // S_EX_PURCHASE_LIMIT_SHOP_ITEM_LIST
	/*0xFE:0x206*/ ExPurchaseLimitShopItemBuy, // S_EX_PURCHASE_LIMIT_SHOP_ITEM_BUY
	/*0xFE:0x207*/ ExBloodyCoinCount, // S_EX_BLOODY_COIN_COUNT
	/*0xFE:0x208*/ ExClaschangeSetAlarm, // S_EX_CLASCHANGE_SET_ALARM,
	/*0xFE:0x209*/ ExRequestClaschange, // S_EX_REQUEST_CLASCHANGE,
	/*0xFE:0x20A*/ ExRequestClaschangeVerifying, // S_EX_REQUEST_CLASCHANGE_VERIFYING,
	/*0xFE:0x20B*/ ExCostumeUseItem, // S_EX_COSTUME_USE_ITEM
	/*0xFE:0x20C*/ ExChooseCostumeItem, // S_EX_CHOOSE_COSTUME_ITEM
	/*0xFE:0x20D*/ ExSendCostumeList, // S_EX_SEND_COSTUME_LIST
	/*0xFE:0x20E*/ ExSendCostumeListFull, // S_EX_SEND_COSTUME_LIST_FULL
	/*0xFE:0x20F*/ ExCostumeCollectionSkillActive, // S_EX_COSTUME_COLLECTION_SKILL_ACTIVE
	/*0xFE:0x210*/ ExCostumeEvolution, // S_EX_COSTUME_EVOLUTION
	/*0xFE:0x211*/ ExCostumeExtract, // S_EX_COSTUME_EXTRACT
	/*0xFE:0x212*/ ExCostumeLock, // S_EX_COSTUME_LOCK
	/*0xFE:0x213*/ ExCostumeShortcutList, // S_EX_COSTUME_SHORTCUT_LIST
	/*0xFE:0x214*/ ExMagicLampExpInfo, // S_EX_MAGICLAMP_EXP_INFO,
	/*0xFE:0x215*/ ExMagicLampGameInfo, // S_EX_MAGICLAMP_GAME_INFO,
	/*0xFE:0x216*/ ExMagicLampGameResult, // S_EX_MAGICLAMP_GAME_RESULT,
	/*0xFE:0x217*/ ExShowTeleportUi, // S_EX_SHOW_TELEPORT_UI,
	/*0xFE:0x218*/ ExActivateAutoShortcut, // S_EX_ACTIVATE_AUTO_SHORTCUT,
	/*0xFE:0x219*/ ExPremiumManagerShowHTML, // S_EX_PREMIUM_MANAGER_SHOW_HTML,
	/*0xFE:0x21A*/ ExActivatedCursedTreasureBoxLocation, // S_EX_ACTIVATED_CURSED_TREASURE_BOX_LOCATION,
	/*0xFE:0x21B*/ ExPaybackList, // S_EX_PAYBACK_LIST,
	/*0xFE:0x21C*/ ExPaybackGiveReward, // S_EX_PAYBACK_GIVE_REWARD,
	/*0xFE:0x21D*/ ExPaybackUiLauncher, // S_EX_PAYBACK_UI_LAUNCHER,
	/*0xFE:0x21E*/ ExDieInfo, // S_EX_DIE_INFO, ddd sdfh
	/*0xFE:0x21F*/ ExAutoplaySetting, // S_EX_AUTOPLAY_SETTING,
	/*0xFE:0x220*/ ExAutoplayDoMacro, // S_EX_AUTOPLAY_DO_MACRO,
	/*0xFE:0x221*/ ExOlympiadMatchMakingResult, //S_EX_OLYMPIAD_MATCH_MAKING_RESULT
	/*0xFE:0x222*/ ExFestivalBMInfo, // S_EX_FESTIVAL_BM_INFO
	/*0xFE:0x223*/ ExFestivalBMAllItemInfo, // S_EX_FESTIVAL_BM_ALL_ITEM_INFO
	/*0xFE:0x224*/ ExFestivalBMTopItemInfo, // S_EX_FESTIVAL_BM_TOP_ITEM_INFO
	/*0xFE:0x225*/ ExFestivalBMGame, // S_EX_FESTIVAL_BM_GAME
	/*0xFE:0x226*/ ExGachaShopInfo, // S_EX_GACHA_SHOP_INFO
	/*0xFE:0x227*/ ExGachaShopGachaGroup, // S_EX_GACHA_SHOP_GACHA_GROUP
	/*0xFE:0x228*/ ExGachaShopGachaItem, // S_EX_GACHA_SHOP_GACHA_ITEM
	/*0xFE:0x229*/ ExTimeRestrictFieldList, // S_EX_TIME_RESTRICT_FIELD_LIST
	/*0xFE:0x22A*/ ExTimeRestrictFieldUserEnter, // S_EX_TIME_RESTRICT_FIELD_USER_ENTER
	/*0xFE:0x22B*/ ExTimeRestrictFieldUserChargeResult, // S_EX_TIME_RESTRICT_FIELD_USER_CHARGE_RESULT
	/*0xFE:0x22C*/ ExTimeRestrictFieldUserAlarm, // S_EX_TIME_RESTRICT_FIELD_USER_ALARM
	/*0xFE:0x22D*/ ExTimeRestrictFieldUserExit, // S_EX_TIME_RESTRICT_FIELD_USER_EXIT
	/*0xFE:0x22E*/ ExRankingCharInfo, // S_EX_RANKING_CHAR_INFO
	/*0xFE:0x22F*/ ExRankingCharHistory, // S_EX_RANKING_CHAR_HISTORY
	/*0xFE:0x230*/ ExRankingCharRankers, // S_EX_RANKING_CHAR_RANKERS
	/*0xFE:0x231*/ ExRankingCharBuffzoneNpcInfo, // S_EX_RANKING_CHAR_BUFFZONE_NPC_INFO
	/*0xFE:0x232*/ ExRankingCharBuffzoneNpcPosition, // S_EX_RANKING_CHAR_BUFFZONE_NPC_POSITION
	/*0xFE:0x233*/ ExBowActionTo, // S_EX_BOW_ACTION_TO
	/*0xFE:0x234*/ ExMercenaryCastlewarCastleInfo, // S_EX_MERCENARY_CASTLEWAR_CASTLE_INFO
	/*0xFE:0x235*/ ExMercenaryCastlewarCastleSiegeHudInfo, // S_EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_HUD_INFO
	/*0xFE:0x236*/ ExMercenaryCastlewarCastleSiegeInfo, // S_EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_INFO
	/*0xFE:0x237*/ ExMercenaryCastlewarCastleSiegeAttackerList, // S_EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_ATTACKER_LIST
	/*0xFE:0x238*/ ExMercenaryCastlewarCastleSiegeDefenderList, // S_EX_MERCENARY_CASTLEWAR_CASTLE_SIEGE_DEFENDER_LIST
	/*0xFE:0x239*/ ExPledgeMercenaryMemberList, // S_EX_PLEDGE_MERCENARY_MEMBER_LIST
	/*0xFE:0x23A*/ ExPvpbookList, // S_EX_PVPBOOK_LIST
	/*0xFE:0x23B*/ ExPvpbookKillerLocation, // S_EX_PVPBOOK_KILLER_LOCATION
	/*0xFE:0x23C*/ ExPvpbookNewPk, // S_EX_PVPBOOK_NEW_PK
	/*0xFE:0x23D*/ ExPledgeMercenaryMemberJoin, // S_EX_PLEDGE_MERCENARY_MEMBER_JOIN
	/*0xFE:0x23E*/ ExRaidDropItemAnnounce, // S_EX_RAID_DROP_ITEM_ANNOUNCE
	/*0xFE:0x23F*/ ExUIPacket, // S_EX_LETTER_COLLECTOR_UI_LAUNCHER
	/*0xFE:0x240*/ ExOlympiadMyRankingInfo, // S_EX_OLYMPIAD_MY_RANKING_INFO
	/*0xFE:0x241*/ ExOlympiadRankingInfo, // S_EX_OLYMPIAD_RANKING_INFO
	/*0xFE:0x242*/ ExOlympiadHeroAndLegendInfo, // S_EX_OLYMPIAD_HERO_AND_LEGEND_INFO
	/*0xFE:0x243*/ ExRaidTeleportInfo, // S_EX_RAID_TELEPORT_INFO
	/*0xFE:0x244*/ ExCraftInfo, // S_EX_CRAFT_INFO
	/*0xFE:0x245*/ ExCraftExtract, // S_EX_CRAFT_EXTRACT
	/*0xFE:0x246*/ ExCraftRandomInfo, // S_EX_CRAFT_RANDOM_INFO
	/*0xFE:0x247*/ ExCraftRandomLockSlot, // S_EX_CRAFT_RANDOM_LOCK_SLOT
	/*0xFE:0x248*/ ExCraftRandomRefresh, // S_EX_CRAFT_RANDOM_REFRESH
	/*0xFE:0x249*/ ExCraftRandomMake, // S_EX_CRAFT_RANDOM_MAKE
	/*0xFE:0x24A*/ ExItemAnnounceSetting, // S_EX_ITEM_ANNOUNCE_SETTING
	/*0xFE:0x24B*/ ExUserBoostStat, // S_EX_USER_BOOST_STAT
	/*0xFE:0x24C*/ ExAntibot, // S_EX_ANTIBOT
	/*0xFE:0x24D*/ ExDPSVR, // S_EX_DPSVR
	/*0xFE:0x24E*/ ExSendCmdList, // S_EX_SEND_CMD_LIST
	/*0xFE:0x24F*/ ExShanghaiHealthyTips, // S_EX_SHANGHAI_HEALTHY_TIPS
	/*0xFE:0x250*/ ExAdenFortressSiegeHUDInfo, // S_EX_ADEN_FORTRESS_SIEGE_HUD_INFO
	/*0xFE:0x251*/ ExPurchaseLimitShopItemListNew, // S_EX_PURCHASE_LIMIT_SHOP_ITEM_LIST_NEW
	/*0xFE:0x252*/ ExSharedPositionSharingUI, // S_EX_SHARED_POSITION_SHARING_UI
	/*0xFE:0x253*/ ExSharedPositionTeleportUI, // S_EX_SHARED_POSITION_TELEPORT_UI
	/*0xFE:0x254*/ ExCharInfo, // S_EX_CHAR_INFO
	/*0xFE:0x255*/ ExAuthReconnect, // S_EX_AUTH_RECONNECT
	/*0xFE:0x256*/ ExShowHomunculusBirthInfo, // S_EX_SHOW_BIRTH_INFO
	/*0xFE:0x257*/ ExHomunculusCreateStartResult, // S_EX_HOMUNCULUS_CREATE_START_RESULT
	/*0xFE:0x258*/ ExHomunculusInsertResult, // S_EX_HOMUNCULUS_INSERT_RESULT
	/*0xFE:0x259*/ ExHomunculusSummonResult, // S_EX_HOMUNCULUS_SUMMON_RESULT
	/*0xFE:0x25A*/ ExShowHomunculusList, // S_EX_SHOW_HOMUNCULUS_LIST
	/*0xFE:0x25B*/ ExDeleteHomunculusDataResult, // S_EX_DELETE_HOMUNCLUS_DATA_RESULT
	/*0xFE:0x25C*/ ExActivateHomunculusResult, // S_EX_ACTIVATE_HOMUNCULUS_RESULT
	/*0xFE:0x25D*/ ExHomunculusGetEnchantPointResult, // S_EX_HOMUNCULUS_GET_ENCHANT_POINT_RESULT
	/*0xFE:0x25E*/ ExHomunculusInitPointResult, // S_EX_HOMUNCULUS_INIT_POINT_RESULT
	/*0xFE:0x25F*/ ExHomunculusPointInfo, // S_EX_HOMUNCULUS_POINT_INFO
	/*0xFE:0x260*/ ExResetHomunculusSkillResult, // S_EX_RESET_HOMUNCULUS_SKILL_RESULT
	/*0xFE:0x261*/ ExEnchantHomunculusSkillResult, // S_EX_ENCHANT_HOMUNCULUS_SKILL_RESULT
	/*0xFE:0x262*/ ExHomunculusEnchantEXPResult, // S_EX_HOMUNCULUS_ENCHANT_EXP_RESULT
	/*0xFE:0x263*/ ExHomunculusHPSPVP, // S_EX_HOMUNCULUS_HPSPVP
	/*0xFE:0x264*/ ExHomunculusReady, // S_EX_HOMUNCULUS_READY
	/*0xFE:0x265*/ ExTeleportFavoritesList, // S_EX_TELEPORT_FAVORITES_LIST
	/*0xFE:0x266*/ //ExVitalExInfo, // S_EX_VITAL_EX_INFO
	/*0xFE:0x267*/ ExNetLatency, // S_EX_NET_LATENCY
	/*0xFE:0x268*/ ExMableGameShowPlayerState, // S_EX_MABLE_GAME_SHOW_PLAYER_STATE
	/*0xFE:0x269*/ ExMableGameDiceResult, // S_EX_MABLE_GAME_DICE_RESULT
	/*0xFE:0x26A*/ ExMableGameMove, // S_EX_MABLE_GAME_MOVE
	/*0xFE:0x26B*/ ExMableGamePrison, // S_EX_MABLE_GAME_PRISON
	/*0xFE:0x26C*/ ExMableGameRewardItem, // S_EX_MABLE_GAME_REWARD_ITEM
	/*0xFE:0x26D*/ ExMableGameSkillInfo, // S_EX_MABLE_GAME_SKILL_INFO
	/*0xFE:0x26E*/ ExMableGameMinigame, // S_EX_MABLE_GAME_MINIGAME
	/*0xFE:0x26F*/ ExMableGamePlayUnable, // S_EX_MABLE_GAME_PLAY_UNABLE
	/*0xFE:0x270*/ ExMableGameUILauncher, // S_EX_MABLE_GAME_UI_LAUNCHER
	/*0xFE:0x271*/ ExMableGameRollCountReset, // S_EX_MABLE_GAME_ROLL_COUNT_RESET
	/*0xFE:0x272*/ ExPetSkillList, // S_EX_PET_SKILL_LIST
	/*0xFE:0x273*/ ExOpenBlessOptionScroll, // S_EX_OPEN_BLESS_OPTION_SCROLL
	/*0xFE:0x274*/ ExBlessOptionPutItem, // S_EX_BLESS_OPTION_PUT_ITEM
	/*0xFE:0x275*/ ExBlessOptionEnchant, // S_EX_BLESS_OPTION_ENCHANT
	/*0xFE:0x276*/ ExBlessOptionCancel, // S_EX_BLESS_OPTION_CANCEL
	/*0xFE:0x277*/ ExPvpRankingMyInfo, // S_EX_PVP_RANKING_MY_INFO
	/*0xFE:0x278*/ ExPvpRankingList, // S_EX_PVP_RANKING_LIST
	/*0xFE:0x279*/ ExPledgeV3Info, // S_EX_PLEDGE_V3_INFO
	/*0xFE:0x27A*/ ExPledgeEnemyInfoList, // S_EX_PLEDGE_ENEMY_INFO_LIST
	/*0xFE:0x27B*/ ExItemDeletionInfo, // S_EX_ITEM_DELETION_INFO
	//272
	/*0xFE:0x27C*/ ExPkPenaltyList, // S_EX_PK_PENALTY_LIST
	/*0xFE:0x27D*/ ExPkPenaltyListOnlyLoc, // S_EX_PK_PENALTY_LIST_ONLY_LOC
	/*0xFE:0x27E*/ ExShowPetExtractSystem, // S_EX_SHOW_PET_EXTRACT_SYSTEM
	/*0xFE:0x27F*/ ExResultPetExtractSystem, // S_EX_RESULT_PET_EXTRACT_SYSTEM
	/*0xFE:0x280*/ ExHidePetExtractSystem, // S_EX_HIDE_PET_EXTRACT_SYSTEM
	//306
	/*0xFE:0x281*/ S_EX_RANKING_FESTIVAL_SIDEBAR_INFO, // S_EX_RANKING_FESTIVAL_SIDEBAR_INFO
	/*0xFE:0x282*/ S_EX_RANKING_FESTIVAL_BUY,
	/*0xFE:0x283*/ S_EX_RANKING_FESTIVAL_BONUS,
	/*0xFE:0x284*/ S_EX_RANKING_FESTIVAL_RANKING,
	/*0xFE:0x285*/ S_EX_RANKING_FESTIVAL_MYINFO,
	/*0xFE:0x286*/ S_EX_RANKING_FESTIVAL_MY_RECEIVED_BONUS,
	/*0xFE:0x287*/ S_EX_RANKING_FESTIVAL_REWARD,
	/*0xFE:0x288*/ ExTimerCheck, //S_EX_TIMER_CHECK,
	/*0xFE:0x289*/ ExSteadyBoxUIInit,
	/*0xFE:0x28A*/ ExSteadyAllBoxUpdate,
	/*0xFE:0x28B*/ ExSteadyOneBoxUpdate,
	/*0xFE:0x28C*/ ExSteadyBoxReward,
	/*0xFE:0x28D*/ ExPetRankingMyInfo, //S_EX_PET_RANKING_MY_INFO,
	/*0xFE:0x28E*/ ExPetRankingList, //S_EX_PET_RANKING_LIST,
	/*0xFE:0x28F*/ ExCollectionInfo,
	/*0xFE:0x290*/ ExCollectionOpenUI,
	/*0xFE:0x291*/ ExCollectionCloseUI,
	/*0xFE:0x292*/ ExCollectionList,
	/*0xFE:0x293*/ ExCollectionUpdateFavorite,
	/*0xFE:0x294*/ ExCollectionFavoriteList,
	/*0xFE:0x295*/ ExCollectionSummary,
	/*0xFE:0x296*/ ExCollectionRegister,
	/*0xFE:0x297*/ ExCollectionComplete,
	/*0xFE:0x298*/ ExCollectionReceiveReward,
	/*0xFE:0x299*/ ExCollectionReset,
	/*0xFE:0x29A*/ ExCollectionActiveEvent,
	/*0xFE:0x29B*/ ExCollectionResetReward,
	/*0xFE:0x29C*/ ExPvpBookShareRevengeList, //S_EX_PVPBOOK_SHARE_REVENGE_LIST,
	/*0xFE:0x29D*/ ExPvpBookShareRevengeKillerLocation, //S_EX_PVPBOOK_SHARE_REVENGE_KILLER_LOCATION,
	/*0xFE:0x29E*/ ExPvpBookShareRevengeNewRevengeInfo, //S_EX_PVPBOOK_SHARE_REVENGE_NEW_REVENGEINFO,
	/*0xFE:0x29F*/ ExPenaltyItemDrop, //S_EX_PENALTY_ITEM_DROP
	/*0xFE:0x2A0*/ ExPenaltyItemList, //S_EX_PENALTY_ITEM_LIST
	/*0xFE:0x2A1*/ ExPenaltyItemRestore, //S_EX_PENALTY_ITEM_RESTORE
	/*0xFE:0x2A2*/ ExUserWatcherTargetList,
	/*0xFE:0x2A3*/ ExUserWatcherTargetStatus,
	/*0xFE:0x2A4*/ S_EX_HOMUNCULUS_ACTIVATE_SLOT_RESULT,
	/*0xFE:0x2A5*/ S_EX_SHOW_HOMUNCULUS_COUPON_UI,
	/*0xFE:0x2A6*/ S_EX_SUMMON_HOMUNCULUS_COUPON_RESULT,
	/*0xFE:0x2A7*/ ExPenaltyItemInfo, //S_EX_PENALTY_ITEM_INFO
	/*0xFE:0x2A8*/ ExMagicSkillUseGround, //S_EX_MAGIC_SKILL_USE_GROUND,
	/*0xFE:0x2A9*/ ExSubjugationSidebar,
	/*0xFE:0x2AA*/ ExSubjugationList,
	/*0xFE:0x2AB*/ ExSubjugationRanking,
	/*0xFE:0x2AC*/ ExSubjugationGachaUI,
	/*0xFE:0x2AD*/ ExSubjugationGacha,
	/*0xFE:0x2AE*/ ExUserViewInfoParameter, //S_EX_USER_VIEW_INFO_PARAMETER
	/*0xFE:0x2AF*/ ExPledgeDonationInfo,
	/*0xFE:0x2B0*/ ExPledgeDonationRequest,
	/*0xFE:0x2B1*/ ExPledgeContributionList,
	/*0xFE:0x2B2*/ ExPledgeRankingMyInfo,
	/*0xFE:0x2B3*/ ExPledgeRankingList,
	/*0xFE:0x2B4*/ ExItemRestoreListPacket,
	/*0xFE:0x2B5*/ ExItemRestorePacket,
	/*0xFE:0x2B6*/ ExPledgeCoinInfo, //S_EX_PLEDGE_COIN_INFO
	/*0xFE:0x2B7*/ S_EX_DETHRONE_INFO,
	/*0xFE:0x2B8*/ S_EX_DETHRONE_RANKING_INFO,
	/*0xFE:0x2B9*/ S_EX_DETHRONE_SERVER_INFO,
	/*0xFE:0x2BA*/ S_EX_DETHRONE_DISTRICT_OCCUPATION_INFO,
	/*0xFE:0x2BB*/ S_EX_DETHRONE_DAILY_MISSION_INFO,
	/*0xFE:0x2BC*/ S_EX_DETHRONE_DAILY_MISSION_GET_REWARD,
	/*0xFE:0x2BD*/ S_EX_DETHRONE_DAILY_MISSION_COMPLETE,
	/*0xFE:0x2BE*/ S_EX_DETHRONE_PREV_SEASON_INFO,
	/*0xFE:0x2BF*/ S_EX_DETHRONE_GET_REWARD,
	/*0xFE:0x2C0*/ S_EX_DETHRONE_CHECK_NAME,
	/*0xFE:0x2C1*/ S_EX_DETHRONE_CHANGE_NAME,
	/*0xFE:0x2C2*/ S_EX_DETHRONE_CONNECT_CASTLE,
	/*0xFE:0x2C3*/ S_EX_DETHRONE_DISCONNECT_CASTLE,
	/*0xFE:0x2C4*/ S_EX_DETHRONE_SEASON_INFO,
	/*0xFE:0x2C5*/ ExServerlimitItemAnnounce, //S_EX_SERVERLIMIT_ITEM_ANNOUNCE,
	/*0xFE:0x2C6*/ ExChangeNickNameColorIcon, //EX_CHANGE_NICKNAME_COLOR_ICON,
	/*0xFE:0x2C7*/ ExWorldcastlewarHostCastleSiegeHudInfo, //EX_WORLDCASTLEWAR_HOST_CASTLE_SIEGE_HUD_INFO
	/*0xFE:0x2C8*/ ExWorldcastlewarCastleInfo, //EX_WORLDCASTLEWAR_CASTLE_INFO
	/*0xFE:0x2C9*/ ExWorldCastlewarCastleSiegeInfo, //EX_WORLDCASTLEWAR_CASTLE_SIEGE_INFO
	/*0xFE:0x2CA*/ ExWorldcastlewarCastleSiegeHudInfo, //EX_WORLDCASTLEWAR_CASTLE_SIEGE_HUD_INFO
	/*0xFE:0x2CB*/ EX_WORLDCASTLEWAR_CASTLE_SIEGE_ATTACKER_LIST,
	/*0xFE:0x2CC*/ EX_WORLDCASTLEWAR_PLEDGE_MERCENARY_MEMBER_LIST,
	/*0xFE:0x2CD*/ EX_WORLDCASTLEWAR_PLEDGE_MERCENARY_MEMBER_JOIN,
	/*0xFE:0x2CE*/ EX_WORLDCASTLEWAR_SIEGE_MAINBATTLE_OCCUPY_INFO,
	/*0xFE:0x2CF*/ EX_WORLDCASTLEWAR_SIEGE_MAINBATTLE_HERO_WEAPON_INFO,
	/*0xFE:0x‭2D0‬*/ EX_WORLDCASTLEWAR_SIEGE_MAINBATTLE_HERO_WEAPON_USER,
	/*0xFE:0x2D1*/ EX_WORLDCASTLEWAR_SIEGE_MAINBATTLE_SIEGE_GOLEM_INFO,
	/*0xFE:0x2D2*/ EX_WORLDCASTLEWAR_SIEGE_MAINBATTLE_DOOR_INFO,
	/*0xFE:0x2D3*/ EX_WORLDCASTLEWAR_SIEGE_MAINBATTLE_HUD_INFO,
	/*0xFE:0x2D4*/ ExPrivateStoreSearchItem, //EX_PRIVATE_STORE_SEARCH_ITEM
	/*0xFE:0x2D5*/ ExPrivateStoreSearchHistory, //EX_PRIVATE_STORE_SEARCH_HISTORY
	/*0xFE:0x2D6*/ ExPrivateStoreSearchStatistics, //EX_PRIVATE_STORE_SEARCH_STATISTICS
	/*0xFE:0x2D9*/ NewHennaList, //EX_NEW_HENNA_LIST
	/*0xFE:0x2DA*/ NewHennaEquip, //EX_NEW_HENNA_EQUIP
	/*0xFE:0x2DB*/ NewHennaUnequip, //EX_NEW_HENNA_UNEQUIP
	/*0xFE:0x2DC*/ NewHennaPotenSelect, //EX_NEW_HENNA_POTEN_SELECT
	/*0xFE:0x2DD*/ NewHennaPotenEnchant, //EX_NEW_HENNA_POTEN_ENCHANT
	/*0xFE:0x2DE*/ NewHennaPotenCompose, //EX_NEW_HENNA_COMPOSE
	/*0xFE:0x2DF*/ ExRequestInviteParty, //EX_REQUEST_INVITE_PARTY,
	/*0xFE:0x2E0‬*/ ExInitGlobalEventUi,
	/*0xFE:0x2E1*/ ExShowGlobalEventUI, //EX_SHOW_GLOBAL_EVENT_UI
	/*0xFE:0x2E2*/ HuntPassSimpleInfo, //EX_L2PASS_SIMPLE_INFO
	/*0xFE:0x2E3*/ HuntPassInfo, //EX_L2PASS_INFO
	/*0xFE:0x2E4*/ HuntPassSayhasSupportInfo, //EX_SAYHAS_SUPPORT_INFO,
	/* 0xFE:0x2E3 */ ExResetEnchantItemFailRewardInfo, // S_EX_RES_ENCHANT_ITEM_FAIL_REWARD_INFO,
	/* 0xFE:0x2E4 */ ExChangedEnchantTargetItemProbabilityList, // S_EX_CHANGED_ENCHANT_TARGET_ITEM_PROB_LIST
	/* 0xFE:0x2E5 */ ExEnchantChallengePointInfo, // S_EX_ENCHANT_CHALLENGE_POINT_INFO
	/* 0xFE:0x2E6 */ ExSetEnchantChallengePoint, // S_EX_SET_ENCHANT_CHALLENGE_POINT
	/* 0xFE:0x2E7 */ ExResetEnchantChallengePoint, // S_EX_RESET_ENCHANT_CHALLENGE_POINT
	/* 0xFE:0x2E8 */ ExResetSelectMultiEnchantScroll, // S_EX_RES_SELECT_MULTI_ENCHANT_SCROLL
	/* 0xFE:0x2E9 */ ExResultSetMultiEnchantItemList, // S_EX_RES_SET_MULTI_ENCHANT_ITEM_LIST
	/* 0xFE:0x2EA */ ExResultMultiEnchantItemList, // S_EX_RES_MULTI_ENCHANT_ITEM_LIST
	/* 0xFE:0x2EB */ ExWorldCastleWarHostSupportPledgeRankingInfo, // S_EX_WORLDCASTLEWAR_HOST_SUPPORT_PLEDGE_RANKING_INFO
	/* 0xFE:0x2EC */ ExWorldCastleWarHostPledgeRankingInfo, // S_EX_WORLDCASTLEWAR_HOST_PLEDGE_RANKING_INFO
	/* 0xFE:0x2ED */ ExWorldCastleWarHostPersonalRankingInfo, // S_EX_WORLDCASTLEWAR_HOST_PERSONAL_RANKING_INFO
	/* 0xFE:0x2EE */ ExWorldCastleWarSupportPledgeRankingInfo, // S_EX_WORLDCASTLEWAR_SUPPORT_PLEDGE_RANKING_INFO
	/* 0xFE:0x2EF */ ExWorldCastleWarPledgeRankingInfo, // S_EX_WORLDCASTLEWAR_PLEDGE_RANKING_INFO
	/* 0xFE:0x2F0 */ ExWorldCastleWarPersonalRankingInfo, // S_EX_WORLDCASTLEWAR_PERSONAL_RANKING_INFO
	/* 0xFE:0x2F1 */ ExHomunculusCreateProbList, // S_EX_HOMUNCULUS_CREATE_PROB_LIST
	/* 0xFE:0x2F2 */ ExHomunculusCouponProbList, // S_EX_HOMUNCULUS_COUPON_PROB_LIST
	/* 0xFE:0x2F3 */ ExWorldCastleWarHostCastleSiegeRankingInfo, // S_EX_WORLDCASTLEWAR_HOST_CASTLE_SIEGE_RANKING_INFO
	/* 0xFE:0x2F4 */ ExWorldCastleWarCastleSiegeRankingInfo, // S_EX_WORLDCASTLEWAR_CASTLE_SIEGE_RANKING_INFO
	/* 0xFE:0x2F5 */ ExMissionLevelRewardList, // S_EX_MISSION_LEVEL_REWARD_LIST
	/* 0xFE:0x2F6 */ ExBalrogWarShowUI, // S_EX_BALROGWAR_SHOW_UI
	/* 0xFE:0x2F7 */ ExBalrogWarShowRanking, // S_EX_BALROGWAR_SHOW_RANKING
	/* 0xFE:0x2F8 */ ExBalrogWarGetReward, // S_EX_BALROGWAR_GET_REWARD
	/* 0xFE:0x2F9 */ ExBalrogWarHUD, // S_EX_BALROGWAR_HUD
	/* 0xFE:0x2FA */ ExBalrogWarBossInfo, // S_EX_BALROGWAR_BOSSINFO
	/* 0xFE:0x2FB */ ExUserRestartLockerList, // S_EX_USER_RESTART_LOCKER_LIST
	/* 0xFE:0x2FC */ ExUserRestartLockerUpdate, // S_EX_USER_RESTART_LOCKER_UPDATE
	/* 0xFE:0x2FD */ ExWorldExchangeItemList, // S_EX_WORLD_EXCHANGE_ITEM_LIST
	/* 0xFE:0x2FE */ ExWorldExchangeRegiItem, // S_EX_WORLD_EXCHANGE_REGI_ITEM
	/* 0xFE:0x2FF */ ExWorldExchangeBuyItem, // S_EX_WORLD_EXCHANGE_BUY_ITEM
	/* 0xFE:0x300 */ ExWorldExchangeSettleList, // S_EX_WORLD_EXCHANGE_SETTLE_LIST
	/* 0xFE:0x301 */ ExWorldExchangeSettleRecvResult, // S_EX_WORLD_EXCHANGE_SETTLE_RECV_RESULT
	/* 0xFE:0x302 */ ExWorldExchangeSellCompleteAlarm, // S_EX_WORLD_EXCHANGE_SELL_COMPLETE_ALARM
	/* 0xFE:0x303 */ ExReadyItemAutoPeel, // S_EX_READY_ITEM_AUTO_PEEL
	/* 0xFE:0x304 */ ExResultItemAutoPeel, // S_EX_RESULT_ITEM_AUTO_PEEL
	/* 0xFE:0x305 */ ExStopItemAutoPeel, // S_EX_STOP_ITEM_AUTO_PEEL

	ExTimeRestrictFieldDieLimitTime, //S_EX_TIME_RESTRICT_FIELD_DIE_LIMT_TIME, //0x306
	ExApplyVariationOption, //S_EX_APPLY_VARIATION_OPTION, //0x307
	S_EX_REQUEST_AUDIO_LOG_SAVE, //0x308
	ExBRVersion, //S_EX_BR_VERSION, //0x309
	S_EX_NOTIFY_ATTENDANCE, //0x30A
	S_EX_WRANKING_FESTIVAL_INFO, //0x30B
	S_EX_WRANKING_FESTIVAL_SIDEBAR_INFO, //0x30C
	S_EX_WRANKING_FESTIVAL_BUY, //0x30D
	S_EX_WRANKING_FESTIVAL_BONUS, //0x30E
	S_EX_WRANKING_FESTIVAL_RANKING, //0x30F
	S_EX_WRANKING_FESTIVAL_MYINFO, //0x310
	S_EX_WRANKING_FESTIVAL_MY_RECEIVED_BONUS, //0x311
	S_EX_WRANKING_FESTIVAL_REWARD, //0x312
	ExTooltipParam, //0x313
	S_EX_RECEIVED_POST_LIST, //0x314
	ExTimeRestrictFieldServerGroup, //0x315
	ExTimeRestrictFieldEnterInfo, //0x316
	S_EX_WORLD_EXCHANGE_SETTLE_ALARM, //0x317
	S_EX_HERO_BOOK_INFO, //0x318
	S_EX_HERO_BOOK_UI, //0x319
	S_EX_HERO_BOOK_CHARGE, //0x31A
	S_EX_HERO_BOOK_ENCHANT, //0x31B
	S_EX_HERO_BOOK_CHARGE_PROB,
	ExTeleportUi, //S_EX_TELEPORT_UI, //0x31C
	S_EX_GOODS_GIFT_CHANGED_NOTI, //0x31D
	S_EX_GOODS_GIFT_LIST_INFO, //0x31E
	S_EX_GOODS_GIFT_ACCEPT_RESULT, //0x31F
	S_EX_GOODS_GIFT_REFUSE_RESULT, //0x320
	S_EX_NONPVPSERVER_NOTIFY_ACTIVATEFLAG, //0x321
	ExWorldExchangeAveragePrice, //S_EX_WORLD_EXCHANGE_AVERAGE_PRICE, //0x322
	ExWorldExchangeTotalList, //S_EX_WORLD_EXCHANGE_TOTAL_LIST, //0x323
	S_EX_PRISON_USER_ENTER, //0x324
	S_EX_PRISON_USER_EXIT, //0x325
	S_EX_PRISON_USER_INFO, //0x326
	S_EX_PRISON_USER_DONATION, //0x327
	ExItemRestoreOpenPacket, //0x328
	S_EX_UNIQUE_GACHA_SIDEBAR_INFO, //1064;
	S_EX_UNIQUE_GACHA_OPEN, //1065;
	S_EX_UNIQUE_GACHA_GAME, //1066;
	S_EX_UNIQUE_GACHA_INVEN_ITEM_LIST, //1067;
	S_EX_UNIQUE_GACHA_INVEN_GET_ITEM, //1068;
	S_EX_UNIQUE_GACHA_INVEN_ADD_ITEM, //1069;
	S_EX_UNIQUE_GACHA_HISTORY, //1070;
	ExFieldDieLimtTime, //S_EX_FIELD_DIE_LIMT_TIME,//1071;
	ExElementalSpiritAttackType, //S_EX_ELEMENTAL_SPIRIT_ATTACK_TYPE,//1072;
	ExGetPledgeCrestPreset, //S_EX_GET_PLEDGE_CREST_PRESET,//1073;
	ExDualInventoryInfo, //1074; S_EX_DUAL_INVENTORY_INFO
	ExSpExtractInfo, //S_EX_SP_EXTRACT_INFO,//1075;
	ExSpExtractItem, //S_EX_SP_EXTRACT_ITEM,//1076;
	ExQuestDialog, //S_EX_QUEST_DIALOG,//1077;
	ExQuestNotification, //S_EX_QUEST_NOTIFICATION,//1078;
	ExQuestNotificationAll, //S_EX_QUEST_NOTIFICATION_ALL,//1079;
	ExQuestUi, //S_EX_QUEST_UI,//1080;
	ExQuestAcceptableAlarm, //S_EX_QUEST_ACCEPTABLE_ALARM,//1081;
	ExQuestAcceptableList, //S_EX_QUEST_ACCEPTABLE_LIST,//1082;
	ExSkillEnchantInfo, //S_EX_SKILL_ENCHANT_INFO,//1083;
	ExSkillEnchantCharge, //S_EX_SKILL_ENCHANT_CHARGE,//1084;
	S_EX_TIME_RESTRICT_FIELD_HOST_USER_ENTER, //1085;
	S_EX_TIME_RESTRICT_FIELD_HOST_USER_LEAVE, //1086;
	S_EX_DETHRONE_SHOP_BUY, //1087;
	S_EX_DETHRONE_POINT_INFO, //1088;
	ExAcquireSkillResult, //S_EX_ACQUIRE_SKILL_RESULT,//1089;
	S_EX_ENHANCED_ABILITY_OF_FIRE_OPEN_UI, //1090;
	S_EX_ENHANCED_ABILITY_OF_FIRE_INIT, //1091;
	S_EX_ENHANCED_ABILITY_OF_FIRE_EXP_UP, //1092;
	S_EX_ENHANCED_ABILITY_OF_FIRE_LEVEL_UP, //1093;
	ExHolyFireOpenUi, //1094;
	S_EX_HOLY_FIRE_NOTIFY, //1095;
	S_EX_PICK_UP_DIST_MODIFY, //1096;
	//439
	ExUserinfoAddPdefend, //S_EX_USERINFO_ADD_PDEFEND,// 1096;
	ExUserinfoAddMdefend, //S_EX_USERINFO_ADD_MDEFEND,// 1097;

	ExVipAttendanceList, //S_EX_VIP_ATTENDANCE_LIST,// 1098;
	ExVipAttendanceCheck, //S_EX_VIP_ATTENDANCE_CHECK,// 1099;
	ExVipAttendanceReward, //S_EX_VIP_ATTENDANCE_REWARD,// 1100;
	ExVipAttendanceNotify, //S_EX_VIP_ATTENDANCE_NOTIFY,// 1101;

	ExMagiclampInfo, //S_EX_MAGICLAMP_INFO,// 1102;
	ExMagiclampResult, //S_EX_MAGICLAMP_RESULT,// 1103;
	ExNewHennaPotenEnchantReset, //S_EX_NEW_HENNA_POTEN_ENCHANT_RESET,// 1104;
	S_EX_INZONE_RANKING_MY_INFO, // 1105;
	S_EX_INZONE_RANKING_LIST, // 1106;
	S_EX_TIME_RESTRICT_FIELD_HOST_USER_LEAVE_BY_NPC, // 1107;
	S_EX_TIME_RESTRICT_FIELD_HOST_USER_ENTER_BY_NPC, // 1108;
	S_EX_PREPARE_LOGIN, // 1110;
	ExRelicsActiveInfo, //S_EX_RELICS_ACTIVE_INFO, //1110
	ExRelicsAnnounce, //S_EX_RELICS_ANNOUNCE, //1111
	ExRelicsList, //S_EX_RELICS_LIST, //1112
	ExRelicsSummonResult, //S_EX_RELICS_SUMMON_RESULT, //1113
	ExRelicsExchangeList, //S_EX_RELICS_EXCHANGE_LIST, //1114
	ExRelicsExchange, //S_EX_RELICS_EXCHANGE, //1115
	ExRelicsExchangeConfirm, //S_EX_RELICS_EXCHANGE_CONFIRM, //1116
	ExRelicsUpgrade, //S_EX_RELICS_UPGRADE, //1117
	ExRelicsCombination, //S_EX_RELICS_COMBINATION, //1118
	ExRelicsCollectionInfo, //S_EX_RELICS_COLLECTION_INFO, //1119
	ExRelicsCollectionUpdate, //S_EX_RELICS_COLLECTION_UPDATE, //1120
	S_EX_SERVERWAR_FIELD_ENTER_USER_INFO, //1121
	S_EX_SERVERWAR_NOTIFY_HOST_HUD_INFO, //1122
	S_EX_SERVERWAR_NOTIFY_HUD_INFO, //1123
	S_EX_SERVERWAR_BATTLE_HUD_INFO, //1124
	S_EX_SERVERWAR_NOTIFY_SET_LEADER, //1125
	S_EX_SERVERWAR_LEADER_LIST, //1126
	S_EX_SERVERWAR_SELECT_LEADER, //1127
	S_EX_SERVERWAR_SELECT_LEADER_INFO, //1128
	S_EX_SERVERWAR_REWARD_ITEM_INFO, //1129
	S_EX_SERVERWAR_REWARD_INFO, //1130
	S_EX_SERVERWAR_GET_REWARD, //1131
	S_EX_SERVERWAR_FIELD_ENTER_HUD_INFO, //1132
	ExRelicsUpdateList, //S_EX_RELICS_UPDATE_LIST, //1133
	ExEnemyKillLog, //S_EX_ENEMY_KILL_LOG, //1134
	ExAllResetRelics, //S_EX_ALL_RESET_RELICS, //1135
	ExRelicsCollectionCompleteAnnounce, //S_EX_RELICS_COLLECTION_COMPLETE_ANNOUNCE, //1136
	S_EX_VIRTUALITEM_SYSTEM_BASE_INFO, //1137
	S_EX_VIRTUALITEM_SYSTEM, //1138
	S_EX_VIRTUALITEM_SYSTEM_POINT_INFO, // 1139
	S_EX_CROSS_EVENT_DATA, //1140
	S_EX_CROSS_EVENT_INFO, //1141
	S_EX_CROSS_EVENT_NORMAL_REWARD, //1142
	S_EX_CROSS_EVENT_RARE_REWARD, //1143
	S_EX_CROSS_EVENT_RESET, //1144
	S_EX_CROSS_EVENT_NOTI, //1145
	ExChatBackgroundSettingNoti, //S_EX_CHAT_BACKGROUND_SETTING_NOTI, //1146
	ExChatBackgroundList, //S_EX_CHAT_BACKGROUND_LIST, //1147

	ExAdenlabBossList, //S_EX_ADENLAB_BOSS_LIST
	ExAdenlabUnlockBoss, //S_EX_ADENLAB_UNLOCK_BOSS
	ExAdenlabBossInfo, //S_EX_ADENLAB_BOSS_INFO
	ExAdenlabNormalSlot, //S_EX_ADENLAB_NORMAL_SLOT
	ExAdenlabNormalPlay, //S_EX_ADENLAB_NORMAL_PLAY
	ExAdenlabSpecialSlot, //S_EX_ADENLAB_SPECIAL_SLOT
	ExAdenlabSpecialProb, //S_EX_ADENLAB_SPECIAL_PROB
	ExAdenlabSpecialPlay, //S_EX_ADENLAB_SPECIAL_PLAY
	ExAdenlabSpecialFix, //S_EX_ADENLAB_SPECIAL_FIX
	ExAdenlabTranscendEnchant, //S_EX_ADENLAB_TRANSCEND_ENCHANT
	ExAdenlabTranscendAnnounce, //S_EX_ADENLAB_TRANSCEND_ANNOUNCE
	ExAdenlabTooltipInfo, //S_EX_ADENLAB_TOOLTIP_INFO
	ExAdenlabTranscendProb, //S_EX_ADENLAB_TRANSCEND_PROB

	S_EX_HOMUNCULUS_EVOLVE, //1158
	S_EX_HOMUNCULUS_SIDEBAR, //1159
	ExExtractSkillEnchant, //S_EX_EXTRACT_SKILL_ENCHANT, //1160
	ExRequestSkillEnchantConfirm, //S_EX_REQUEST_SKILL_ENCHANT_CONFIRM, //1161
	ExItemScore, //S_EX_ITEM_SCORE, //1162
	S_EX_VITALITY_KEEP_VITALPOINT_INFO, //1163
	ExPopupEventHud, // S_EX_POPUP_EVENT_HUD 1164
	ExWorldExchangeInfo, //1165
	ExDamagePopup, //S_EX_DAMAGE_POPUP 1166
	S_EX_LEVELAMBIENCE_CHANGEDINFO, //1167
	ExCreateItemProbList, //S_EX_CREATE_ITEM_PROB_LIST, //1171;
	ExCraftSlotProbList, //S_EX_CRAFT_SLOT_PROB_LIST, //1172;
	ExNewHennaComposeProbList, //S_EX_NEW_HENNA_COMPOSE_PROB_LIST, //1173;
	ExVariationProbList, //S_EX_VARIATION_PROB_LIST, //1174;
	ExRelicsProbList, //S_EX_RELICS_PROB_LIST, //1175;
	ExUpgradeSystemProbList, //S_EX_UPGRADE_SYSTEM_PROB_LIST, //1176;
	ExCombinationProbList, //S_EX_COMBINATION_PROB_LIST, //1177;

	ExNewHennaPotenOpenslotProbInfo, //1179;
	ExNewHennaPotenOpenslot, //1180;
	ExAcquirePetSkillResult, //1181;
	ExRelicsSummonList, //1182;
	ExRelicsPointInfo, //1183;
	ExBalrogwarTopRank, //1184;
	ExDyeeffectList, //1185;
	ExDyeeffectEnchantProbInfo, //1186;
	ExDyeeffectEnchantNormalskill, //1187;
	ExDyeeffectAcquireHiddenskill, //1188;
	ExDyeeffectEnchantReset, //1189;
	ExLoadPetPreview, //1190;

	ExMatchinginzoneNotifyHudInfo,
	ExMatchinginzoneFieldEnterUserInfo,
	ExRaidAuctionDropEffect,
	ExRaidAuctionBidInfo,
	ExRaidAuctionResult,
	ExRaidAuctionPostAlarm,
	ExRaidAuctionPostList,
	ExBreakEquipmentNoti,
	ExRepairAllEquipment,
	ExHpEffect,
	ExClassChangeUiOpen,
	ExClassChangeFail,
	ExBlessOptionProbList,
	ExCompress,

	ExMax, //1191;
	;

	public static final ServerPacketOpcodes507[] VALUES = values();

	private final int _id;
	private final int _exId;

	private ServerPacketOpcodes507()
	{
		int ordinal = ordinal();
		if(ordinal >= 0xFE)
		{
			_id = 0xFE;
			_exId = ordinal - 0xFE;
		}
		else
		{
			_id = ordinal;
			_exId = -1;
		}
	}

	public int getId()
	{
		return _id;
	}

	public int getExId()
	{
		return _exId;
	}
}