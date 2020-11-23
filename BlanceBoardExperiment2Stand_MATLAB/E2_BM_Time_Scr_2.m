%Screen('Preference', 'SkipSyncTests', 1);
tic;
clear;clc;close;
tbegin=clock;
prompt={'实验编号:','姓名：', 'ntrials'};
name='Expemental information';
numlines=1;
defaultanswer={'','','5'};
answer=inputdlg(prompt,name,numlines,defaultanswer);
subject=answer{1};
subname=answer{2}; 
ntrials=str2num(answer{3});

BMfile='BMfiles\Walk.txt';
if ~exist('BMfile','var')
    [f, p] = uigetfile({'*.txt','Biological Motion file (*.txt)'},...
        'Select the Biological Motion file...','BMfiles\Walk.txt');
    BMfile = fullfile(p, f);
end
[BM3D, frame, marker, Hz]=LoadBMfile(BMfile);% frame=30

%% Prepare stimuli
AssertOpenGL;
whichscreen=max(Screen('Screens'));
DotSize=7;
amplifier=0.6;
types=6;
proangle=[linspace(0,pi/3,types/2) linspace(pi*2/3,pi,types/2)];%projection angle relative to x (y=0) plane
ST.DotSize=DotSize;
ST.amplifier=amplifier;
ST.proangle=proangle;

 j=1;

markershow=[1:13];%head(1)-shoulders(2/3)-elbows(4/5)-hands(6/7)-waist(8/9)-knees(10/11)-feet(12/13)
BM_Upr=zeros(length(markershow),2,frame,length(proangle));
BM_Inv=zeros(length(markershow),2,frame,length(proangle));
NormilizedX=BM3D(markershow,1,:);
NormilizedY=BM3D(markershow,3,:);
rangex=round((max(NormilizedX(:))-min(NormilizedX(:)))/2);
rangey=round((max(NormilizedY(:))-min(NormilizedY(:)))/2);
randshiftx=PsychRandSample([-rangex:rangex],[length(markershow),types]);
randshifty=PsychRandSample([-rangey:rangey],[length(markershow),types]);
for i=1:types
    BM_Upr(:,2,:,i)=-(BM3D(markershow,3,:)-(max(NormilizedY(:))+min(NormilizedY(:)))/2)*amplifier;
    BM_Inv(:,2,:,i)=(BM3D(markershow,3,:)-(max(NormilizedY(:))+min(NormilizedY(:)))/2)*amplifier;
    BM_Upr(:,2,:,i)=-(BM3D(markershow,3,:)-(max(NormilizedY(:))+min(NormilizedY(:)))/2+...
        repmat(randshifty(:,i),[1,1,frame])+repmat(squeeze(BM_Upr(:,2,1,i)),[1,1,frame]))*amplifier;
    BM_Inv(:,2,:,i)=(BM3D(markershow,3,:)-(max(NormilizedY(:))+min(NormilizedY(:)))/2+...
        repmat(randshifty(:,i),[1,1,frame])-repmat(squeeze(BM_Inv(:,2,1,i)),[1,1,frame]))*amplifier;
    BM_Upr(:,1,:,i)=(BM3D(markershow,1,:)*cos(proangle(i))+BM3D(markershow,2,:)*sin(proangle(i)))*amplifier;
    BM_Inv(:,1,:,i)=(BM3D(markershow,1,:)*cos(proangle(i))+BM3D(markershow,2,:)*sin(proangle(i)))*amplifier;
    BM_Upr(:,1,:,i)=(BM3D(markershow,1,:)*cos(proangle(i))+BM3D(markershow,2,:)*sin(proangle(i))+...
        repmat(randshiftx(:,i),[1,1,frame])-repmat(squeeze(BM_Upr(:,1,1,i)),[1,1,frame]))*amplifier;
    BM_Inv(:,1,:,i)=(BM3D(markershow,1,:)*cos(proangle(i))+BM3D(markershow,2,:)*sin(proangle(i))+...
        repmat(randshiftx(:,i),[1,1,frame])-repmat(squeeze(BM_Inv(:,1,1,i)),[1,1,frame]))*amplifier;
end


black=BlackIndex(whichscreen);
white=WhiteIndex(whichscreen);
gray=round((black+white)/2);

% Screen('Resolution',whichscreen,[],[],60);
[windowPtr, rect]=Screen('OpenWindow',whichscreen,gray,[0 0 1024 768]);
Screen('BlendFunction', windowPtr, GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
[centerx,centery]=RectCenter(rect);
ST.centerx=centerx;
ST.centery=centery;
ST.rect=rect;
defaultwin=Screen('MakeTexture', windowPtr, imread('Fixation.bmp'));
beginwin=Screen('MakeTexture', windowPtr, imread('begin.jpg'));
restwin=Screen('MakeTexture', windowPtr, imread('rest.jpg'));
continuewin=Screen('MakeTexture', windowPtr, imread('continue.jpg'));
thankswin=Screen('MakeTexture', windowPtr, imread('thanks.jpg'));


fps=Screen('NominalFrameRate',windowPtr);% 60
ifi=Screen('GetFlipInterval',windowPtr);
waitframes=round(fps/Hz);% 2=60/30=fps/frame
ST.waitframes=waitframes;
ST.fps=fps;
ST.ifi=ifi;

KbName('UnifyKeyNames');
Key1 = KbName('LeftArrow');
Key2 = KbName('RightArrow');
escapeKey = KbName('ESCAPE');
spaceKey = KbName('SPACE');
[touch, secs, keyCode] = KbCheck;

breakall=0;% 0=not break

StandDuration=1;
DurationType=[0.4 0.6 0.8 1 1.2 1.4 1.6];
frameStandard=StandDuration/((1/fps)*waitframes);
frameCompare=DurationType./((1/fps)*waitframes);

ST.StandDuration=StandDuration;
ST.DurationType=DurationType;
ST.frameStandard=frameStandard;
ST.frameCompare=frameCompare;

UprOrder=1:2; %1=the 1st is upr
UprType=1:2;% 1=the 1st is standard duration
Duration=1:length(DurationType);
stimseq0=CombineFactors(UprOrder,UprType,Duration,1:ntrials);
stimseq=stimseq0(Shuffle(1:size(stimseq0,1)),:);
results=zeros(size(stimseq,1),30);
%% Show stimuli
HideCursor;
Screen(windowPtr,'FillRect',gray);
Screen('Flip', windowPtr);

Screen('DrawTexture',windowPtr,beginwin);
beginonset=Screen('Flip', windowPtr);
while 1
    [touch, secs, keyCode] = KbCheck;
    if keyCode(spaceKey)
        break
    end
end
beginoffset=Screen('Flip',windowPtr);

% msg='请按空格键开始';
% errinfo=ShowInstruction_UMNVAL(windowPtr, rect, msg, spaceKey, gray, white, -200);
% if errinfo==1
%     Screen('CloseAll');
%     return
% end
% oldPriority=Priority(MaxPriority(windowPtr));
% vbl=Screen('Flip',windowPtr);
   Priority(2);

for trial=1:size(stimseq,1)
    results(trial,1:3)=stimseq(trial,1:3);
    initframe=PsychRandSample(1:frame,[2,1]);% a initframe for each PLW(2 PLWs in the exp)
    BM_Show=zeros(marker,2,frame,2);
    results(trial,21)=initframe(1);
    results(trial,22)=initframe(2);
    if results(trial,1)==1 % Upright is the first PLW
        BM_Show(:,:,:,1)=BM_Upr(:,:,:,PsychRandSample(1:types));
        BM_Show(:,:,:,2)=BM_Inv(:,:,:,PsychRandSample(1:types));
    elseif results(trial,1)==2 % Upr is the second PLW
        BM_Show(:,:,:,1)=BM_Inv(:,:,:,PsychRandSample(1:types));
        BM_Show(:,:,:,2)=BM_Upr(:,:,:,PsychRandSample(1:types));
    end
    
    if results(trial,2)==1 % first PLW is stand
        frame1=frameStandard;
        frame2=frameCompare(results(trial,3));
        Duration1=StandDuration;
        Duration2=DurationType(results(trial,3));
    elseif results(trial,2)==2
        frame2=frameStandard;
        frame1=frameCompare(results(trial,3));
        Duration1=DurationType(results(trial,3));
        Duration2=StandDuration;
    end
    trial_num=trial;
    % present fixation
    trialinterval= PsychRandSample(10:15,[1,1]) *0.1;
    Screen('DrawTexture',windowPtr,defaultwin);
   tTrial(trial,:)=clock;
    fixonset(trial)=Screen('Flip', windowPtr);% onset
    fixoffset(trial)=Screen('Flip', windowPtr,fixonset(trial)+trialinterval);% the time of offset

    % present the first PLW
    for i=1:frame1
        Screen('DrawDots',windowPtr,squeeze(BM_Show(:,:,  mod(i+initframe(1),frame)+1,1))',DotSize,white,[centerx centery],1);
        if i==1 vbl_0= Screen('Flip',windowPtr);  vbl_1(trial)=vbl_0; end
        if i> 1 vbl_0= Screen('Flip', windowPtr, vbl_0+(waitframes-0.5)*ifi); end
    end
    vbl_2(trial)= Screen('Flip', windowPtr, vbl_0+(waitframes-0.5)*ifi);
    results(trial,8)=vbl_2(trial)-vbl_1(trial);% check the duration of the 1st PLW
    
    
    % blank between two PLWs
    blankinterval=PsychRandSample(4:6,[1,1])*0.1;
    Screen(windowPtr,'FillRect',gray);
  
   blankonset(trial)=Screen('Flip', windowPtr);
   blankoffset(trial)=Screen('Flip', windowPtr,blankonset(trial)+blankinterval);
    
    % present the second PLW
  
    for j=1:frame2
        Screen('DrawDots',windowPtr,squeeze(BM_Show(:,:,  mod(j+initframe(2),frame)+1,2))',DotSize,white,[centerx centery],1);
        if j==1 vbl_0= Screen('Flip',windowPtr);  vbl_3(trial)=vbl_0; end
        if j> 1 vbl_0= Screen('Flip', windowPtr, vbl_0+(waitframes-0.5)*ifi); end
    end
    vbl_4(trial)= Screen('Flip', windowPtr, vbl_0+(waitframes-0.5)*ifi);
 
    results(trial,9)=vbl_4(trial)-vbl_3(trial);
    
    
    results(trial,4)=-999;
       
    
    % give a response

    Screen(windowPtr,'FillRect',gray);
    Screen('Flip', windowPtr);
    trialtime=GetSecs;  
    responseB(trial)=trialtime;
    while  results(trial,4)==-999 % make sure that the response is valid only after the display of 2nd PLW is done
        RestrictKeysForKbCheck([Key1 Key2 escapeKey spaceKey]);
        [touch, secs, keyCode] = KbCheck;
        if keyCode(Key1) % the 1s plw is longer---left key
            results(trial,4)=1;
            results(trial,5)=secs-trialtime;
        elseif keyCode(escapeKey)
            breakall=1;
            fprintf('quitting program by user!\n');
            break
        elseif keyCode(Key2) % the 2nd PLW is longer----right key
            results(trial,4)=2;
            results(trial,5)=secs-trialtime;
        end
    end
    responseEnd(trial)=Screen('Flip', windowPtr);
   
    if breakall==1
        break
    end
    
    % have a rest
    if mod(trial,28)==0 && trial~=size(stimseq,1)
        rest_time=10;  %休息的时间长度
        Screen('DrawTexture',windowPtr,restwin);
        onsetrest(j)=Screen('Flip', windowPtr);
        offsetrest=Screen('Flip', windowPtr,onsetrest(j)+ rest_time);
        Screen('DrawTexture',windowPtr,continuewin);%continue
        continueonset=Screen('Flip', windowPtr);
        while 1
            [touch, secs, keyCode] = KbCheck;
            if keyCode(spaceKey)
                break
            end
        end
        continueoffset(j)=Screen('Flip',windowPtr);     
        j=j+1;
    end
end
tend=clock;
Priority(0);
ShowCursor;
% ListenChar;
TimingRest=[onsetrest',continueoffset'];
Timing=[tTrial,fixonset',fixoffset',vbl_1',vbl_2',blankonset',blankoffset',vbl_3',vbl_4',responseB',responseEnd'];

Screen('DrawTexture',windowPtr,thankswin);
%  DrawFormattedText(windowPtr,double('十分感谢您的参与!'),300,300,white);
thanksT=3;
onsetT=Screen('Flip', windowPtr);
offsetT=Screen('Flip', windowPtr,thanksT+onsetT);
% WaitSecs(thanksT);
Screen('CloseAll');
dirName = 'E2@202011';
if ~exist(dirName,'dir')
    mkdir(dirName);
end
datafile=sprintf('%s\\%s_%s_%s_%s',dirName,subject,mfilename,name,datestr(now,30));
save(datafile,'results','tbegin','tend','Timing','TimingRest','trial','stimseq','ntrials','proangle','ST');
BMTime_Analysis(datafile);