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
import { IChatUser, ChatUser } from 'app/shared/model/chat-user.model';
import { ChatUserService } from './chat-user.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IChatPhoto } from 'app/shared/model/chat-photo.model';
import { ChatPhotoService } from 'app/entities/chat-photo/chat-photo.service';

@Component({
  selector: 'jhi-chat-user-update',
  templateUrl: './chat-user-update.component.html'
})
export class ChatUserUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  chatphotos: IChatPhoto[];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    bannedUser: [],
    userId: [],
    chatPhotoId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected chatUserService: ChatUserService,
    protected userService: UserService,
    protected chatPhotoService: ChatPhotoService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ chatUser }) => {
      this.updateForm(chatUser);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.chatPhotoService
      .query({ 'chatUserId.specified': 'false' })
      .pipe(
        filter((mayBeOk: HttpResponse<IChatPhoto[]>) => mayBeOk.ok),
        map((response: HttpResponse<IChatPhoto[]>) => response.body)
      )
      .subscribe(
        (res: IChatPhoto[]) => {
          if (!this.editForm.get('chatPhotoId').value) {
            this.chatphotos = res;
          } else {
            this.chatPhotoService
              .find(this.editForm.get('chatPhotoId').value)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IChatPhoto>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IChatPhoto>) => subResponse.body)
              )
              .subscribe(
                (subRes: IChatPhoto) => (this.chatphotos = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(chatUser: IChatUser) {
    this.editForm.patchValue({
      id: chatUser.id,
      creationDate: chatUser.creationDate != null ? chatUser.creationDate.format(DATE_TIME_FORMAT) : null,
      bannedUser: chatUser.bannedUser,
      userId: chatUser.userId,
      chatPhotoId: chatUser.chatPhotoId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const chatUser = this.createFromForm();
    if (chatUser.id !== undefined) {
      this.subscribeToSaveResponse(this.chatUserService.update(chatUser));
    } else {
      this.subscribeToSaveResponse(this.chatUserService.create(chatUser));
    }
  }

  private createFromForm(): IChatUser {
    return {
      ...new ChatUser(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      bannedUser: this.editForm.get(['bannedUser']).value,
      userId: this.editForm.get(['userId']).value,
      chatPhotoId: this.editForm.get(['chatPhotoId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChatUser>>) {
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackChatPhotoById(index: number, item: IChatPhoto) {
    return item.id;
  }
}
