import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IChatInvitation, ChatInvitation } from 'app/shared/model/chat-invitation.model';
import { ChatInvitationService } from './chat-invitation.service';
import { IChatUser } from 'app/shared/model/chat-user.model';
import { ChatUserService } from 'app/entities/chat-user/chat-user.service';
import { IChatRoom } from 'app/shared/model/chat-room.model';
import { ChatRoomService } from 'app/entities/chat-room/chat-room.service';
import { IChatNotification } from 'app/shared/model/chat-notification.model';
import { ChatNotificationService } from 'app/entities/chat-notification/chat-notification.service';

@Component({
  selector: 'jhi-chat-invitation-update',
  templateUrl: './chat-invitation-update.component.html'
})
export class ChatInvitationUpdateComponent implements OnInit {
  isSaving: boolean;

  chatusers: IChatUser[];

  chatrooms: IChatRoom[];

  chatnotifications: IChatNotification[];

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    acceptance: [],
    denial: [],
    acceptanceDenialDate: [],
    senderId: [],
    receiverId: [],
    chatRoomId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected chatInvitationService: ChatInvitationService,
    protected chatUserService: ChatUserService,
    protected chatRoomService: ChatRoomService,
    protected chatNotificationService: ChatNotificationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ chatInvitation }) => {
      this.updateForm(chatInvitation);
    });
    this.chatUserService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChatUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChatUser[]>) => response.body)
      )
      .subscribe((res: IChatUser[]) => (this.chatusers = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.chatRoomService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChatRoom[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChatRoom[]>) => response.body)
      )
      .subscribe((res: IChatRoom[]) => (this.chatrooms = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.chatNotificationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChatNotification[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChatNotification[]>) => response.body)
      )
      .subscribe((res: IChatNotification[]) => (this.chatnotifications = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(chatInvitation: IChatInvitation) {
    this.editForm.patchValue({
      id: chatInvitation.id,
      creationDate: chatInvitation.creationDate != null ? chatInvitation.creationDate.format(DATE_TIME_FORMAT) : null,
      acceptance: chatInvitation.acceptance,
      denial: chatInvitation.denial,
      acceptanceDenialDate:
        chatInvitation.acceptanceDenialDate != null ? chatInvitation.acceptanceDenialDate.format(DATE_TIME_FORMAT) : null,
      senderId: chatInvitation.senderId,
      receiverId: chatInvitation.receiverId,
      chatRoomId: chatInvitation.chatRoomId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const chatInvitation = this.createFromForm();
    if (chatInvitation.id !== undefined) {
      this.subscribeToSaveResponse(this.chatInvitationService.update(chatInvitation));
    } else {
      this.subscribeToSaveResponse(this.chatInvitationService.create(chatInvitation));
    }
  }

  private createFromForm(): IChatInvitation {
    return {
      ...new ChatInvitation(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      acceptance: this.editForm.get(['acceptance']).value,
      denial: this.editForm.get(['denial']).value,
      acceptanceDenialDate:
        this.editForm.get(['acceptanceDenialDate']).value != null
          ? moment(this.editForm.get(['acceptanceDenialDate']).value, DATE_TIME_FORMAT)
          : undefined,
      senderId: this.editForm.get(['senderId']).value,
      receiverId: this.editForm.get(['receiverId']).value,
      chatRoomId: this.editForm.get(['chatRoomId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChatInvitation>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackChatUserById(index: number, item: IChatUser) {
    return item.id;
  }

  trackChatRoomById(index: number, item: IChatRoom) {
    return item.id;
  }

  trackChatNotificationById(index: number, item: IChatNotification) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
