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
import { IChatRoomAllowedUser, ChatRoomAllowedUser } from 'app/shared/model/chat-room-allowed-user.model';
import { ChatRoomAllowedUserService } from './chat-room-allowed-user.service';
import { IChatRoom } from 'app/shared/model/chat-room.model';
import { ChatRoomService } from 'app/entities/chat-room/chat-room.service';
import { IChatUser } from 'app/shared/model/chat-user.model';
import { ChatUserService } from 'app/entities/chat-user/chat-user.service';

@Component({
  selector: 'jhi-chat-room-allowed-user-update',
  templateUrl: './chat-room-allowed-user-update.component.html'
})
export class ChatRoomAllowedUserUpdateComponent implements OnInit {
  isSaving: boolean;

  chatrooms: IChatRoom[];

  chatusers: IChatUser[];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    bannedUser: [],
    bannedDate: [],
    chatRoomId: [],
    chatUserId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected chatRoomAllowedUserService: ChatRoomAllowedUserService,
    protected chatRoomService: ChatRoomService,
    protected chatUserService: ChatUserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ chatRoomAllowedUser }) => {
      this.updateForm(chatRoomAllowedUser);
    });
    this.chatRoomService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChatRoom[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChatRoom[]>) => response.body)
      )
      .subscribe((res: IChatRoom[]) => (this.chatrooms = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.chatUserService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IChatUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChatUser[]>) => response.body)
      )
      .subscribe((res: IChatUser[]) => (this.chatusers = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(chatRoomAllowedUser: IChatRoomAllowedUser) {
    this.editForm.patchValue({
      id: chatRoomAllowedUser.id,
      creationDate: chatRoomAllowedUser.creationDate != null ? chatRoomAllowedUser.creationDate.format(DATE_TIME_FORMAT) : null,
      bannedUser: chatRoomAllowedUser.bannedUser,
      bannedDate: chatRoomAllowedUser.bannedDate != null ? chatRoomAllowedUser.bannedDate.format(DATE_TIME_FORMAT) : null,
      chatRoomId: chatRoomAllowedUser.chatRoomId,
      chatUserId: chatRoomAllowedUser.chatUserId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const chatRoomAllowedUser = this.createFromForm();
    if (chatRoomAllowedUser.id !== undefined) {
      this.subscribeToSaveResponse(this.chatRoomAllowedUserService.update(chatRoomAllowedUser));
    } else {
      this.subscribeToSaveResponse(this.chatRoomAllowedUserService.create(chatRoomAllowedUser));
    }
  }

  private createFromForm(): IChatRoomAllowedUser {
    return {
      ...new ChatRoomAllowedUser(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      bannedUser: this.editForm.get(['bannedUser']).value,
      bannedDate:
        this.editForm.get(['bannedDate']).value != null ? moment(this.editForm.get(['bannedDate']).value, DATE_TIME_FORMAT) : undefined,
      chatRoomId: this.editForm.get(['chatRoomId']).value,
      chatUserId: this.editForm.get(['chatUserId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChatRoomAllowedUser>>) {
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

  trackChatRoomById(index: number, item: IChatRoom) {
    return item.id;
  }

  trackChatUserById(index: number, item: IChatUser) {
    return item.id;
  }
}
