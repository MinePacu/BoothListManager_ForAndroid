import string
import gspread
from gspread.cell import rowcol_to_a1
from gspread.cell import a1_to_rowcol
import re
from enum import Enum
from string import ascii_uppercase
from datetime import datetime
from gspread.utils import MergeType, ValueInputOption, ValueRenderOption
from gspread_formatting.models import Borders
import gspread_formatting

credentials = {
}

gc : gspread.client.Client = None
sheet : gspread.spreadsheet.Spreadsheet = None
worksheet: gspread.spreadsheet.Worksheet = None
nowgid = -1

sheetId = "16yv58pKn7pQgU3z-SnSXytFp3XWRP57mlMNisc4WAG8"

# 부스들의 열 리스트 설정 (해당 변수들은 예시로 제3회 일러스타 페스의 값들임)
thrid_illustarfes_alphabet_list = list(ascii_uppercase)
thrid_illustarfes_alphabet_list.append('가')
thrid_illustarfes_alphabet_list.append('나')
thrid_illustarfes_alphabet_list.append('다')
thrid_illustarfes_alphabet_list.append('라')
thrid_illustarfes_alphabet_list.append('마')
thrid_illustarfes_alphabet_list.append('바')

seoul_comic_alphabet_list = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S']

alphabet_list = thrid_illustarfes_alphabet_list


#  부스 열별 최댓값 리스트
third_illustar_fes_booth_max_count = [20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 40, 40, 40, 40, 40, 40]
seoul_comic_world_booth_max_count = [41, 24, 24, 26, 26, 26, 24, 24, 24, 28, 28, 28, 28, 28, 28, 28, 28, 28, 28]

alphabet_max_count = third_illustar_fes_booth_max_count

# 예시 딕셔너리
seoul_comic_alphabet_max_count_dict = dict(zip(seoul_comic_alphabet_list, seoul_comic_world_booth_max_count))
# example_al = 'A'
# print("Debug Dict : " + str(seoul_comic_alphabet_max_count_dict[example_al]))


# 부스 목록 시트의 Id
test_illustar_fes_sheet = "1CJ-K_6nBLhgyPbVSKSuq5T9tHAF-RT80WKLDmAI5NeQ"
test_seoul_comic_sheet_id = "1-lbwfQONKVZ9wD5HXpCjiQ6gElYYU-RZ3ziD0LdrnyE"

spreadsheetId = test_illustar_fes_sheet


# 부스 목록 시트 안에서 선입금 시트
sheetName_illustar_fes = "선입금, 통판, 인포 목록의 사본"
sheetNumber_illustar_fes = 0

seoul_comic_sheetName = "선입금 목록"
seoul_comic_sheetNumber = 0

sheetName = sheetName_illustar_fes
sheetNumber = sheetNumber_illustar_fes

# 선입금 시트 내의 행 인덱싱
BoothNumber_Col_Number = 2
BoothName_Col_Number = 3
Genre_Col_Number = 4
Yoil_Col_Number = 5
InfoLabel_Col_Number = 6
InfoLink_Col_Number = 6
Pre_Order_Date_Col_Number = 7
Pre_Order_Label_Col_Number = 8
Pre_Order_link_Col_Number = 8

BoothNumber_Col_Alphabet = 'B'
BoothName_Col_Alphabet = 'C'
Yoil_Col_Alphabet = 'E'
InfoLabel_Link_Col_Alphabet = 'F'
Pre_Order_link_Col_Alphabet = 'H'
Etc_Point_Col_Alphabet = 'I'

# 선입금 시트 내의 열 인덱싱
UpdateTime_Row_Number = 1

# 업데이트 로그 시트
UpdateLogSheetName = "업데이트 내용"
UpdateLogSheetNumber = 1

# 행사 지도 시트
MapSheetNumber = None

# 기타 비교용 변수
IsAlredyExisted = False

sheetStartIndex = 3
# 업데이트 로그의 가장 최신 로그가 자리할 열의 위치
updateSheetStartIndex = 5

# 한 행에 있는 데이터의 줄 수
dateline_In_aRow = 1

# 선입금, 통판, 수요조사 시트에서 다중 줄을 포함하는 행의 커스텀 높이 값
sheetRowHeightPerLine = 17

updateLogType = 4

# 자동 위치 계산 기능을 무시할지 여부
isIgnoreRecommandLocation = False

class LogType(Enum):
	Pre_Order = 1
	Mail_Order = 2
	Info = 3
	Etc = 4

def loginService():
	try:
		global gc
		gc = gspread.service_account_from_dict(credentials)
		gc_ = gc
		return gc_
	except gspread.exceptions.APIError:
		return None

def getSheet(sheetId : string):
	try:
		global sheet
		global gc
		sheet = gc.open_by_key(sheetId)
		sheet_ = sheet
		return sheet_
	except gspread.exceptions.APIError:
		return None

def getWorkSheet(sheetId : string, sheetNumber : int):
	try:
		global gc
		global nowgid
		global sheet
		global worksheet
		if sheet == None:
			sheet = gc.open_by_key(sheetId)
		worksheet = sheet.get_worksheet(sheetNumber)
		return worksheet
	except gspread.exceptions.APIError:
		return None

def addBoothInfoToSheet(boothnumber : string, boothname : string, genre : string, yoil : string,
						infoLabel : string, infoLink : string, preorder_Date : string,
						preorder_Label : string, preorder_Link : string):
	dateline_In_aRow = 1	

	NewBoothGenre = f''
	if '//' in genre:
		NewBoothGenre = f'=TEXTJOIN(CHAR(10), 0, '
		SplitedGenre = re.split('//', genre)
		if len(SplitedGenre) > dateline_In_aRow:
			dateline_In_aRow = len(SplitedGenre)
		i = 0
		for OnelineGenre in SplitedGenre:
			NewBoothGenre += f'"{OnelineGenre}'
			if i != len(SplitedGenre) - 1:
				NewBoothGenre += f'", '
			else:
				NewBoothGenre += f'")'
			i = i + 1
	else:
		NewBoothGenre = genre

	# 부스 선입금 마감 일자 함수 생성 (다중 줄 포함)
	NewPreOrderDate = f''
	if '//' in preorder_Date:
		NewPreOrderDate = f'=TEXTJOIN(CHAR(10), 0, '
		SplitedPreOrderDate = re.split('//', preorder_Date)
		if len(SplitedPreOrderDate) > dateline_In_aRow:
			dateline_In_aRow = len(SplitedPreOrderDate)
		i = 0
		for OnelinePreOrderDate in SplitedPreOrderDate:
			NewPreOrderDate += f'"{OnelinePreOrderDate}'
			if i != len(SplitedPreOrderDate) - 1:
				NewPreOrderDate += f'", '
			else:
				NewPreOrderDate += f'")'
			i = i + 1
		# print(NewPreOrderDate)
	else:
		NewPreOrderDate = preorder_Date

	NewInfoLabel = AddTextJoin(infoLabel, isAddEqualLetter=False)

	# 부스 인포 링크 함수 생성
	NewInfoLink = f''
	if (infoLink != ''):
		NewInfoLink = f'=HYPERLINK("{infoLink}", {NewInfoLabel})'

	NewPreOrderLabel = AddTextJoin(preorder_Label, isAddEqualLetter=False)

	# 부스 선입금 링크 함수 생성
	NewPreOrderLink = f''
	if (preorder_Link != ''):
		NewPreOrderLink = f'=HYPERLINK("{preorder_Link}", {NewPreOrderLabel})'

	# print(BoothNumber, BoothName, Genre, Yoil, InfoLabel, InfoLink, Pre_Order_label, Pre_Order_Link)

	global gc
	client_ = gc

	print("Add_new_BoothData : 셀 전체 데이터를 가져오는 중...")
	sh = client_.open_by_key(sheetId)
	print(f"sh : {sh}")

	booth_list = sh.get_worksheet(sheetNumber).get(f"{BoothNumber_Col_Alphabet}1:{BoothNumber_Col_Alphabet}")
	updatesheet = sh.get_worksheet(UpdateLogSheetNumber)
	fmt = gspread_formatting.CellFormat(
			borders=Borders(
				top=gspread_formatting.Border("SOLID"),
				bottom=gspread_formatting.Border("SOLID"),
				left=gspread_formatting.Border("SOLID"),
				right=gspread_formatting.Border("SOLID")
			),
			horizontalAlignment='CENTER',
			verticalAlignment='MIDDLE'
		)

	if (boothnumber != ''):
		sheet = sh.get_worksheet(sheetNumber)

		booth_list_tmp = booth_list.copy()
		# print(booth_list_tmp[13])
		j = 0
		for boothnum in booth_list_tmp:
			if len(boothnum) == 0:
				booth_list_tmp[j] = booth_list_tmp[j - 1]
			if ',' in str(booth_list_tmp[j][0]):
				booth_list_tmp[j] = booth_list_tmp[j][0].split(", ")
			j = j + 1

		RecommandLocation = int(getRecommandLocation(booth_list_tmp, boothnumber)) if isIgnoreRecommandLocation == False else 0
		print(f"GetRecommandLocation : {RecommandLocation}")
		NewRowData = ['', boothnumber, boothname, NewBoothGenre, yoil, NewInfoLink, NewPreOrderDate, NewPreOrderLink]

		if int(RecommandLocation) == 0:
			sheet.insert_row(NewRowData, sheetStartIndex, value_input_option=ValueInputOption.user_entered)
			gspread_formatting.format_cell_range(sheet,
										f"{BoothNumber_Col_Alphabet}{len(booth_list) + 1}:{Etc_Point_Col_Alphabet}{len(booth_list) + 1}",
										fmt)
			if dateline_In_aRow != 1:
				sheet.rows_auto_resize(sheetStartIndex, sheetStartIndex)
			else:
				gspread_formatting.set_row_height(sheet, str(sheetStartIndex), 30)


			updatetime = SetUpdateDates()
			if preorder_Label != "":
				hyperLinkCell = f"CONCATENATE(\"#gid={sheet.id}&range={Pre_Order_link_Col_Alphabet}\", MATCH(\"{boothname}\", \'{sheet.title}\'!{BoothName_Col_Alphabet}:{BoothName_Col_Alphabet}, 0))"
				AddUpdateLog(updatesheet, LogType(updateLogType), updatetime, sheet.id,
				 			hyperLinkCell, boothnumber, BoothName=boothname, LinkName=preorder_Label)
			else:
				hyperLinkCell = f"CONCATENATE(\"#gid={sheet.id}&range={InfoLabel_Link_Col_Alphabet}\", MATCH(\"{boothname}\", \'{sheet.title}\'!{BoothName_Col_Alphabet}:{BoothName_Col_Alphabet}, 0))"
				AddUpdateLog(updatesheet, LogType(updateLogType), updatetime, sheet.id,
				 			hyperLinkCell, boothnumber, BoothName=boothname, LinkName=infoLabel)

			if MapSheetNumber != None:
				SetLinkToMap(boothnumber)
			return True

		else:
			sheet.insert_row(NewRowData, RecommandLocation, value_input_option=ValueInputOption.user_entered)
			gspread_formatting.format_cell_range(sheet,
										f"{BoothNumber_Col_Alphabet}{RecommandLocation}:{Etc_Point_Col_Alphabet}{RecommandLocation}",
										fmt)
			if dateline_In_aRow != 1:
				sheet.rows_auto_resize(RecommandLocation, RecommandLocation)
			else:
				gspread_formatting.set_row_height(sheet, str(RecommandLocation), 30)

			updatetime = SetUpdateDates()
			if preorder_Label != "":
				hyperLinkCell = f"CONCATENATE(\"#gid={sheet.id}&range={Pre_Order_link_Col_Alphabet}\", MATCH(\"{boothname}\", \'{sheet.title}\'!{BoothName_Col_Alphabet}:{BoothName_Col_Alphabet}, 0))"
				AddUpdateLog(updatesheet, LogType(updateLogType), updatetime, sheet.id,
							hyperLinkCell, boothnumber, BoothName=boothname, LinkName=preorder_Label)
			else:
				hyperLinkCell = f"CONCATENATE(\"#gid={sheet.id}&range={InfoLabel_Link_Col_Alphabet}\", MATCH(\"{boothname}\", \'{sheet.title}\'!{BoothName_Col_Alphabet}:{BoothName_Col_Alphabet}, 0))"
				AddUpdateLog(updatesheet, LogType(updateLogType), updatetime, sheet.id,
							hyperLinkCell, boothnumber, BoothName=boothname, LinkName=infoLabel)

			global IsAlredyExisted
			if IsAlredyExisted == True:
				k = 0 
				for k in range(RecommandLocation - 1 - 1, -1, -1):
					if len(booth_list[k]) != 0:
						break

				sheet.merge_cells(f"{BoothNumber_Col_Alphabet}{k + 1}:{Yoil_Col_Alphabet}{RecommandLocation}",
													MergeType.merge_columns)

			if MapSheetNumber != None:
				SetLinkToMap(boothnumber)
			return True

	else:
		sheet_ = sh.get_worksheet(sheetNumber)
		NewRowData = ['', boothnumber, boothname, NewBoothGenre, yoil, NewInfoLink, NewPreOrderDate, NewPreOrderLink]
		print(f"RowData : {NewRowData}")
		sheet_.insert_row(NewRowData, sheetStartIndex, value_input_option=ValueInputOption.user_entered)
		gspread_formatting.format_cell_range(sheet_,
									   f"{BoothNumber_Col_Alphabet}{sheetStartIndex}:{Etc_Point_Col_Alphabet}{sheetStartIndex}",
										 fmt)
		
		if (dateline_In_aRow != 1):
			sheet_.rows_auto_resize(sheetStartIndex, sheetStartIndex)
		else:
			gspread_formatting.set_row_height(sheet_, str(sheetStartIndex), 30)

		updatetime = SetUpdateDates()
		if preorder_Label != "":
			hyperLinkCell = f"CONCATENATE(\"#gid={sheet_.id}&range={Pre_Order_link_Col_Alphabet}\", MATCH(\"{boothname}\", \'{sheet_.title}\'!{BoothName_Col_Alphabet}:{BoothName_Col_Alphabet}, 0))"
			AddUpdateLog(updatesheet, LogType(updateLogType), updatetime, sheet_.id,
									 hyperLinkCell, BoothName=boothname, LinkName=preorder_Label)
		else:
			hyperLinkCell = f"CONCATENATE(\"#gid={sheet_.id}&range={InfoLabel_Link_Col_Alphabet}\", MATCH(\"{boothname}\", \'{sheet_.title}\'!{BoothName_Col_Alphabet}:{BoothName_Col_Alphabet}, 0))"
			AddUpdateLog(updatesheet, LogType(updateLogType), updatetime, sheet_.id,
								 hyperLinkCell, BoothName=boothname, LinkName=infoLabel)

		if MapSheetNumber != None:
			SetLinkToMap(boothnumber)
		return True

	print("Add_new_BoothData : 부스 추가 완료")

def add_UpdateLog(boothname: string, linkname: string, offset: int):
	updatetime = SetUpdateDates()
	print("updatetime : " + str(updatetime))

	sh = gc.open_by_key(sheetId)
	print("sh : " + str(sh))
	sheet_ = sh.get_worksheet(sheetNumber)
	print("sheet_ : " + str(sheet_))
	updatesheet = sh.get_worksheet(UpdateLogSheetNumber)
	print("updatesheet : " + str(updatesheet))

	hyperlinkCell = f"CONCATENATE(\"#gid={sheet_.id}&range={Pre_Order_link_Col_Alphabet}\", SUM(MATCH(\"{boothname}\", \'{sheet_.title}\'!{BoothName_Col_Alphabet}:{BoothName_Col_Alphabet}, 0), {str(offset)}))"
	AddUpdateLog(updatesheet, LogType.Etc, updatetime, sheet_.id, hyperlinkCell, BoothName=boothname, LinkName=linkname)
	return True

def getRecommandLocation(booth_list_tmp: list[str], searchBoothNum: str):
	"""
	매개 변수인 부스 번호가 어느 셀에 들어가야할지를 계산하여 반환합니다.
	이미 있는 부스 번호인 경우, 해당 부스 번호가 있는 셀의 위치의 아래 행 위치를 반환합니다.
	단, 해당 알파벳 열의 부스가 하나도 등록되지 않은 경우, 0을 반환합니다. (이건 생각하기 힘들어서... 어차피 나 혼자 쓰는데 뭐..)

	부스 번호는 [알파벳 + 숫자] 형식을 가집니다. (예 : W10)

	:param str booth_list_tmp: 부스 번호들의 리스트, 이 리스트에 빈 요소는 없어야 합니다.
	:param str searchBoothNum: 추가할 부스 번호
	"""
	print("GetRecommandLocation : 부스를 추가할 셀 위치 계산 중...")
	# Recommanded Cell's Location
	conclusionLocation = ""
	IsFind = False
	IsAlreadyAdded = False
	global IsAlredyExisted
	IsAlredyExisted = False
	AlreadyExistedLocation = ""

	if ',' in searchBoothNum:
		searchBoothNum = searchBoothNum.split(", ")[0]

	for k in range(0, len(booth_list_tmp)):
		if (booth_list_tmp[k].count(searchBoothNum) >= 1):
			# 중복인 경우, 이미 등록된 부스가 있는 열 + 1의 값을 반환하여 바로 추가할 수 있도록 함
			IsAlreadyAdded = True
			AlreadyExistedLocation = str(k + 1 + 1)  # 열 번호는 1부터 시작 + 다음 위치로 계산해서 +1 한번 더

	if IsAlreadyAdded == False:
		userSector = re.findall('[a-zA-Z]', searchBoothNum)
		print(f"userSector after [a-zA-Z] : {userSector}")
		if len(userSector) == 0:
			print("userSector is \"\"")
			userSector = re.sub(r"[^ㄱ-ㅣ가-힣\s]", "", searchBoothNum)
		userSectorNum = re.findall('\d', searchBoothNum)
		
		print(f"userSector : {userSector}")
		print(f"userSectorNum : {userSectorNum}")

		userSectorNum_tmp = ""
		for k in userSectorNum:
			userSectorNum_tmp += k

			# print(userSector)
		# print(userSectorNum_tmp)

		# 해당 부스의 이전 값부터 탐색
		for i in range(1, int(userSectorNum_tmp) - 1):
			try:
				# 추가하려는 부스의 이전 번호의 부스를 검색 (이는 2칸 전, 3칸 전일 수 있음)
				autoSearchBoothNum = userSector[0] + str(int(userSectorNum_tmp) - i)
				print("GetRecommandLocation : 검색 중인 부스 번호 : " + autoSearchBoothNum)
				for j in range(0, len(booth_list_tmp)):
					try:
						Index_ = booth_list_tmp[j].index(autoSearchBoothNum)
						Index = j
						break
					except:
						continue

				# 검색 중인 부스가 여러 개의 행을 병합한 경우
				if (booth_list_tmp.count(booth_list_tmp[Index]) > 1):
					iterated_Indexes = find_duplicating_Indexes(booth_list_tmp, booth_list_tmp[Index])
					conclusionLocation = str(booth_list_tmp[iterated_Indexes[len(iterated_Indexes) - 1]] + 1)
					IsFind = True

				# 검색 중인 부스가 한 개의 행을 보유한 경우
				elif (booth_list_tmp.count(booth_list_tmp[Index]) == 1):
					conclusionLocation = str(Index + 1)  # 0으로 시작해서 + 1
					IsFind = True
				break
			except:
				continue

		# 찾지 못한 경우, 해당 부스의 다음 값으로 탐색
		if IsFind == False:
			# 예) W10 이라 하면 1 ~ (25(W열의 최대값) - 10 - 1 = 14) 으로 W11부터 W25까지 탐색
			for i in range(1, alphabet_max_count[alphabet_list.index(userSector[0])] - int(userSectorNum_tmp) - 1):
				try:
					autoSearchBoothNum = userSector[0] + str(int(userSectorNum_tmp) + i)
					print("GetRecommandLocation : 검색 중인 부스 번호 : " + autoSearchBoothNum)
					for j in range(0, len(booth_list_tmp)):
						try:
							Index_ = booth_list_tmp[j].index(autoSearchBoothNum)
							Index = j
							break
						except:
							continue

					# 검색 중인 부스가 여러 개의 행을 병합한 경우
					if (booth_list_tmp.count(booth_list_tmp[Index]) > 1):
						iterated_Indexes = find_duplicating_Indexes(booth_list_tmp, booth_list_tmp[Index])
						conclusionLocation = str(booth_list_tmp[iterated_Indexes[len(iterated_Indexes) - 1]] + 1)
						IsFind = True

					# 검색 중인 부스가 한 개의 행을 보유한 경우
					elif (booth_list_tmp.count(booth_list_tmp[Index]) == 1):
						conclusionLocation = str(Index + 1)  # 0으로 시작해서 + 1에 새로 한 행을 만들어야하므로 + 1 한 번 더
						IsFind = True
					break
				except:
					continue

		# 그냥 없으면 수동으로 하자.
		if IsFind == False:
			return 0

		print("GetRecommandLocation : 계산된 열 위치 : " + conclusionLocation)
		return conclusionLocation

	else:
		print("GetRecommandLocation : 이미 있는 부스입니다. 이 부스가 있는 다음 열로 배정합니다. 계산된 열 위치 : " + AlreadyExistedLocation)
		IsAlredyExisted = True
		return AlreadyExistedLocation


def AddUpdateLog(sheet: gspread.Worksheet, logtype: LogType, updatetime: datetime, sheetid_hyperlink: str,
								 HyperLinkCell: str, BoothNumber: str = None, BoothName: str = None, IsOwnAuthor: bool = False,
								 AuthorNickName: str = None, LinkName: str = None):
	"""
	업데이트 로그를 추가합니다.

	- 매개 변수
			:param sheet: 업데이트 로그를 추가할 시트
			:param logtype: 업데이트 로그의 추가 타입으로 선입금인 Pre_Order, 통판인 Mail_Order 둘 중 하나의 값입니다.
			:param updatetime: 업데이트 시간
			:param sheetid_hyperlink: 업데이트된 부스 정보가 위치한 시트의 Id
			:param HyperLinkCell: 업데이트된 부스 정보의 선입금 또는 통판 또는 인포 링크가 담긴 셀의 a1Notation 값
			:param BoothNumber: (선택) 업데이트된 부스의 부스 번호, 이 값이 None인 경우, BoothName은 None이 아니여야 합니다.
			:param BoothName: (선택) 업데이트된 부스의 부스 이름, 이 값이 None인 경우, BoothNumber은 None이 아니여야 합니다.
			:param IsOwnAuthor: (선택) 업데이트된 정보가 특정 작가님별로 업데이트된 정보인지 여부입니다. 기본값은 False입니다.
			:param AuthorNickName: (선택) IsOwnAuthor의 값이 True인 경우에 사용되며, 작가님의 닉네임입니다. 기본값은 None입니다.
			:param LinkName: (선택 ) logtype이 Etc인 경우, 링크 이름을 지정할 수 있습니다.
	"""

	# 업데이트 로그가 B행부터 시작이면 이렇게, 아닌 경우 이걸 수정
	updatelog_data = ['']

	# 업데이트 로그의 시각과 내용이 들어갈 행의 알파벳
	UpdateLogtime_ColAlphabet = 'B'
	UpdateLog_ColAlphabet = 'C'

	updatelog_time = updatetime
	updatelog_data.append(
		f'{updatelog_time.month}.{updatelog_time.day} {updatelog_time.hour}:{str(updatelog_time.minute).zfill(2)}:{str(updatelog_time.second).zfill(2)}')

	updatelog_string = f''
	if logtype == LogType.Pre_Order:
		if IsOwnAuthor == True:
			if BoothNumber != None:
				updatelog_string = f'=HYPERLINK({HyperLinkCell}, "{BoothNumber} 부스의 {AuthorNickName} 작가님의 선입금 링크 추가")'
			elif BoothName != None:
				updatelog_string = f'=HYPERLINK({HyperLinkCell}, "{BoothName} 부스의 {AuthorNickName} 작가님의 선입금 링크 추가")'
		else:
			if BoothNumber != None:
				updatelog_string = f'=HYPERLINK({HyperLinkCell}, "{BoothNumber} 부스의 선입금 링크 추가")'
			elif BoothName != None:
				updatelog_string = f'=HYPERLINK({HyperLinkCell}, "{BoothName} 부스의 선입금 링크 추가")'

	elif logtype == LogType.Mail_Order:
		if IsOwnAuthor == True:
			if BoothNumber != None:
				updatelog_string = f'=HYPERLINK({HyperLinkCell}, "{BoothNumber} 부스의 {AuthorNickName} 작가님의 통판 링크 추가")'
			elif BoothName != None:
				updatelog_string = f'=HYPERLINK({HyperLinkCell}, "{BoothName} 부스의 {AuthorNickName} 작가님의 통판 링크 추가")'
		else:
			if BoothNumber != None:
				updatelog_string = f'=HYPERLINK({HyperLinkCell}, "{BoothNumber} 부스의 통판 링크 추가")'
			elif BoothName != None:
				updatelog_string = f'=HYPERLINK({HyperLinkCell}, "{BoothName} 부스의 통판 링크 추가")'

	elif logtype == LogType.Info:
		if IsOwnAuthor == True:
			if BoothNumber != None:
				updatelog_string = f'=HYPERLINK({HyperLinkCell}, "{BoothNumber} 부스의 {AuthorNickName} 작가님의 인포 추가")'
			elif BoothName != None:
				updatelog_string = f'=HYPERLINK({HyperLinkCell}, "{BoothName} 부스의 {AuthorNickName} 작가님의 인포 추가")'
		else:
			if BoothNumber != None:
				updatelog_string = f'=HYPERLINK({HyperLinkCell}, "{BoothNumber} 부스의 인포 추가")'
			elif BoothName != None:
				updatelog_string = f'=HYPERLINK({HyperLinkCell}, "{BoothName} 부스의 인포 추가")'

	elif logtype == LogType.Etc:
		if IsOwnAuthor == True:
				updatelog_string = f'=HYPERLINK({HyperLinkCell}, "{BoothName} 부스의 {AuthorNickName} 작가님의 {LinkName} 추가")'
		else:
				updatelog_string = f'=HYPERLINK({HyperLinkCell}, "{BoothName} 부스의 {LinkName} 추가")'


	updatelog_data.append(updatelog_string)

	sheet.insert_row(updatelog_data, updateSheetStartIndex, value_input_option=ValueInputOption.user_entered)
	fmt = gspread_formatting.CellFormat(
		borders=Borders(
			top=gspread_formatting.Border("SOLID"),
			bottom=gspread_formatting.Border("SOLID"),
			left=gspread_formatting.Border("SOLID"),
			right=gspread_formatting.Border("SOLID")
		),
		horizontalAlignment='CENTER',
		verticalAlignment='MIDDLE',
	)

	gspread_formatting.format_cell_range(sheet,
										f"{UpdateLogtime_ColAlphabet}{updateSheetStartIndex}:{UpdateLog_ColAlphabet}{updateSheetStartIndex}",
										fmt)
	gspread_formatting.set_row_height(sheet, str(updateSheetStartIndex), 30)


def SetUpdateDates():
	"""
	시트 내에 있는 마지막 업데이트 시간 셀을 업데이트한 후, 업데이트한 시간을 반환합니다.
	해당 셀의 위치는 a1Notation을 기준으로 셀의 행을 나타내는 전역 변수 BoothNumber_Col_Alphabet, 열을 나타내는 전역 변수 UpdateTime_Row_Number 값에 의해 결정됩니다.
	"""
	print("UpdateLastestTime : 셀 전체 데이터를 가져오는 중...")
	sh = gc.open_by_key(sheetId)
	sheet = sh.get_worksheet(sheetNumber)

	print("UpdateLastestTime : 업데이트 시간 반영 중...")
	updatetime = datetime.now()
	sheet.update_acell(f'{BoothNumber_Col_Alphabet}{UpdateTime_Row_Number}',
										 f'마지막 업데이트 시간 : {updatetime.year}. {updatetime.month}. {updatetime.day} {updatetime.hour}:{str(updatetime.minute).zfill(2)}:{str(updatetime.second).zfill(2)}')

	return updatetime

def moveBoothData(originIndex: int, moveIndex: int):
	"""
	전역 변수 sheetId와 sheetNumber에 의해 지정된 워크 시트에서 매개 변수에 의해 지정된 인덱스에 있는 부스 데이터를 특정 인덱스로 이동합니다.

	:param originIndex 이동시킬 부스 데이터가 있는 인덱스
	:param moveIndex 이동시킬 위치 인덱스
	"""
	sh  = gc.open_by_key(sheetId)
	sheet = sh.get_worksheet(sheetNumber)

	originBoothData = sheet.get(f"{BoothNumber_Col_Alphabet}{originIndex}:{Pre_Order_link_Col_Alphabet}{originIndex}", value_render_option=ValueRenderOption.formula)
	originBoothData[0].insert(0, '')
	# print(originBoothData)

	sheet.insert_row(originBoothData[0], moveIndex, value_input_option=ValueInputOption.user_entered)
	if originIndex > moveIndex : # 이동하려는 위치가 원래 위치보다 위인 경우
		sheet.delete_rows(originIndex + 1)
	elif originIndex < moveIndex : # 이동하려는 위치가 원래 위치보다 아래인 경우
		sheet.delete_rows(originIndex)

	return True

def putBoothNumbertoSpecificBooth(boothname: str, boothnumber: str):
	"""
	매개 변수 boothname에 해당하는 부스 데이터에 지정한 부스 번호 (boothnumber) 를 할당합니다.
	해당 부스를 찾지 못한 경우, None을 반환합니다.

	:param boothname 부스 번호를 할당할 부스의 이름
	:param boothnumber 할당할 부스 번호, 일반적으로 [부스 코드] + [숫자] 조합이며, [부스 코드]에 소문자 알파벳이 있을 경우, 자동으로 대문자로 변환합니다. 
	"""
	sh  = gc.open_by_key(sheetId)
	sheet = sh.get_worksheet(sheetNumber)

	uppered_boothnumber = boothnumber.upper()

	cell = sheet.find(boothname)
	if cell != None:
		return sheet.update_acell(f"{BoothNumber_Col_Alphabet}{cell.row}", uppered_boothnumber)
	else:
		return None  


def SetLinkToMap(BoothNumber: str):
	"""
	부스 번호 셀과 부스 지도에서의 해당 부스 위치 셀을 서로 링크합니다.

	- 매개 변수
			:param BoothNumber: 서로 링크할 부스 번호
	"""
	sheet = gc.open_by_key(sheetId)
	BoothListSheet = sheet.get_worksheet(sheetNumber)
	BoothMapSheet = sheet.get_worksheet(MapSheetNumber)

	BoothNumberCell_Data = BoothMapSheet.find(BoothNumber)

	if ',' in BoothNumber:
		BoothNumber_splited = BoothNumber.split(',')
		for i in len(BoothNumber_splited):
			BoothNumber_splited[i].replace(" ", "")

		# key => 지도에서의 해당 부스의 a1 위치 값, value => 부스 위치에서의 a1 위치 값
		BoothLocations = []
		for Number in BoothNumber_splited:
			MapLocationData = BoothMapSheet.find(Number)
			BoothLocations.append(rowcol_to_a1(MapLocationData.row, MapLocationData.col))

			BoothMapSheet.update_acell(rowcol_to_a1(MapLocationData.row, MapLocationData.col),
							  		f'=HYPERLINK("#gid{BoothListSheet.id}&range={rowcol_to_a1(BoothNumberCell_Data.row, BoothNumberCell_Data.col)}", "{MapLocationData.value}")')

		BoothListSheet.update_acell(rowcol_to_a1(BoothNumberCell_Data.row, BoothNumberCell_Data.col),
							  		f'=HYPERLINK("#gid={BoothMapSheet.id}&range={BoothLocations[0]}:{BoothLocations[len(BoothLocations) - 1]}", "{BoothNumber}")')

def find_duplicating_Indexes(_List, searchWord: str):
	iterated_index_position_list = [
		i for i in range(len(_List)) if _List[i] == searchWord
	]
	return iterated_index_position_list

def AddTextJoin(label: str, isAddEqualLetter: bool = True):
	"""
	지정한 `label`에 `//` 가 있는 경우, 스프레드시트의 `TextJoin` 함수를 사용하여 줄을 바꿉니다.
	`//` 가 없는 경우에는 원래 매개 변수 `label` 값을 그대로 반환합니다.
	또한 전역 변수 `dateLine_In_aRow` 값은 이 함수가 수행하기 전의 값보다 `label`의 줄 수가 많은 경우 갱신됩니다.

	:param label TextJoin 함수를 적용할 문자열
	:param isAddEqualLetter `=` 기호를 넣을지 여부
	"""
	global dateline_In_aRow
	if '//' in label:
		NewLabel= f'=TEXTJOIN(CHAR(10), 0, ' if isAddEqualLetter == True else f'TEXTJOIN(CHAR(10), 0, '
		SplitedLabel = re.split('//', label)
		if len(SplitedLabel) > dateline_In_aRow:
			dateline_In_aRow = len(SplitedLabel)
		i = 0
		for OnelineLabel in SplitedLabel:
			NewLabel += f'"{OnelineLabel}'
			if i != len(SplitedLabel) - 1:
				NewLabel += f'", '
			else:
				NewLabel += f'")'
			i = i + 1
		return NewLabel
	else:
		NewLabel = f"\"{label}\""
		return NewLabel
