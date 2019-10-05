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
import { IChatNotification, ChatNotification } from 'app/shared/model/chat-notification.model';
import { ChatNotificationService } from './chat-notification.service';
import { IChatUser } from 'app/shared/model/chat-user.model';
import { ChatUserService } from 'app/entities/chat-user/chat-user.service';
import { IChatInvitation } from 'app/shared/model/chat-invitation.model';
import { ChatInvitationService } from 'app/entities/chat-invitation/chat-invitation.service';
import { IChatRoom } from 'app/shared/model/chat-room.model';
import { ChatRoomService } from 'app/entities/chat-room/chat-room.service';
import { IChatMessage } from 'app/shared/model/chat-message.model';
import { ChatMessageService } from 'app/entities/chat-message/chat-message.service';

@Component({
  selector: 'jhi-chat-notification-update',
  templateUrl: './chat-notification-update.component.html'
})
export class ChatNotificationUpdateComponent implements OnInit {
  isSaving: boolean;

  chatusers: IChatUser[];

  chatinvitations: IChatInvitation[];

  chatrooms: IChatRoom[];

  chatmessages: IChatMessage[];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    chatNotificationReason: [],
    chatUserId: [],
    chatInvitations: [],
    chatRoomId: [],
    chatMessageId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected chatNotificationService: ChatNotificationService,
    protected chatUserService: ChatUserService,
    protected chatInvitationService: ChatInvitationService,
    protected chatRoomService: ChatRoomService,
    protected chatMessageService: ChatMessageService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ chatNotification }) => {
      this.updateForm(chatNotification);
    });
    this.chatUserService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChatUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChatUser[]>) => response.body)
      )
      .subscribe((res: IChatUser[]) => (this.chatusers = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.chatInvitationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChatInvitation[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChatInvitation[]>) => response.body)
      )
      .subscribe((res: IChatInvitation[]) => (this.chatinvitations = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.chatRoomService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChatRoom[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChatRoom[]>) => response.body)
      )
      .subscribe((res: IChatRoom[]) => (this.chatrooms = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.chatMessageService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChatMessage[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChatMessage[]>) => response.body)
      )
      .subscribe((res: IChatMessage[]) => (this.chatmessages = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(chatNotification: IChatNotification) {
    this.editForm.patchValue({
      id: chatNotification.id,
      creationDate: chatNotification.creationDate != null ? chatNotification.creationDate.format(DATE_TIME_FORMAT) : null,
      chatNotificationReason: chatNotification.chatNotificationReason,
      chatUserId: chatNotification.chatUserId,
      chatInvitations: chatNotification.chatInvitations,
      chatRoomId: chatNotification.chatRoomId,
      chatMessageId: chatNotification.chatMessageId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const chatNotification = this.createFromForm();
    if (chatNotification.id !== undefined) {
      this.subscribeToSaveResponse(this.chatNotificationService.update(chatNotification));
    } else {
      this.subscribeToSaveResponse(this.chatNotificationService.create(chatNotification));
    }
  }

  private createFromForm(): IChatNotification {
    return {
      ...new ChatNotification(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      chatNotificationReason: this.editForm.get(['chatNotificationReason']).value,
      chatUserId: this.editForm.get(['chatUserId']).value,
      chatInvitations: this.editForm.get(['chatInvitations']).value,
      chatRoomId: this.editForm.get(['chatRoomId']).value,
      chatMessageId: this.editForm.get(['chatMessageId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChatNotification>>) {
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

  trackChatInvitationById(index: number, item: IChatInvitation) {
    return item.id;
  }

  trackChatRoomById(index: number, item: IChatRoom) {
    return item.id;
  }

  trackChatMessageById(index: number, item: IChatMessage) {
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
